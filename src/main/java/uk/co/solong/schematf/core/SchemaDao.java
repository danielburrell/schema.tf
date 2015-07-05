package uk.co.solong.schematf.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.RemoteRefUpdate.Status;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.solong.schematf.auth.Credentials;
import uk.co.solong.steam4j.tf2.data.schema.TF2Schema;
import uk.co.solong.steam4j.tf2.data.schema.TF2SchemaItem;
import uk.co.solong.steam4j.tf2.data.schema.TF2SchemaQuality;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class SchemaDao {
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final CacheLoader<String, JsonNode> loader;
    private final LoadingCache<String, JsonNode> cache;
    private static final Logger logger = LoggerFactory.getLogger(SchemaDao.class);
    private final String directory;
    private final Credentials credentials;
    private final CredentialsProvider credentialsProvider;
    private static final String CACHE_SCHEMA_KEY = "latest";
    private static final String CACHE_ITEMS_KEY = "items";
    private static final String CACHE_QUALITIES_KEY = "qualities";
    private static final String TF2_SCHEMA_FOLDER = "tf2";
    private static final String TF2_SCHEMA_FILE = "schema.json";
    private static final String TF2_SCHEMA_FULLPATH = File.separator + TF2_SCHEMA_FOLDER + File.separator + TF2_SCHEMA_FILE;
    private static final String ORIGIN = "origin";
    private static final String MASTER = "master";
    private static final String ORIGIN_MASTER = "origin/master";

    /**
     * Persists the schema to git by committing it and pushing it out.<br/>
     * Cache is invalidated.
     * 
     * @param node
     * @throws CannotCommitException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public void persist(JsonNode node) throws CannotCommitException, JsonGenerationException, JsonMappingException, IOException {
        saveSchemaContents(node);
        commitAndPush();
        cacheInvalidate();
    }

    /**
     * Fetch the latest schema from the cache (or load it from the local
     * repository if not available).
     * 
     * @return
     * @throws ExecutionException
     */
    public JsonNode getLatestSchema() throws ExecutionException {
        return cache.get(CACHE_SCHEMA_KEY);
    }

    /**
     * If another instance of this service has already published a newer schema,
     * fetch the latest data from origin and then drop any local commits we have
     * made. Invalidate the cache too.
     * 
     * @throws NoMessageException
     * @throws UnmergedPathsException
     * @throws ConcurrentRefUpdateException
     * @throws WrongRepositoryStateException
     * @throws GitAPIException
     * @throws IOException
     */
    public void discardAndResync() throws NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException, WrongRepositoryStateException,
            GitAPIException, IOException {
        // drop all changes. update to the latest. invalidate the cache
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.readEnvironment().findGitDir(new File(directory)).build();
        Git git = null;
        try {
            git = new Git(repository);
            git.fetch().setRemote(ORIGIN).call();
            git.reset().setMode(ResetType.HARD).setRef(ORIGIN_MASTER).call();
        } finally {
            if (git != null) {
                git.close();
            }
        }
        cacheInvalidate();
    }

    private boolean doesLocalRepoExist() {
        File f = new File(directory);
        return (f.exists());
    }

    private JsonNode loadDataFromDisk(String key) throws JsonProcessingException, IOException {
        // load the schema from disk
        switch (key) {
       
        case CACHE_SCHEMA_KEY:
            return jsonMapper.readTree(new File(directory + TF2_SCHEMA_FULLPATH));
        case CACHE_ITEMS_KEY:
            return getItemsFromSchema(jsonMapper.readTree(new File(directory + TF2_SCHEMA_FULLPATH)));
        case CACHE_QUALITIES_KEY:
            return getQualitiesFromSchema(jsonMapper.readTree(new File(directory + TF2_SCHEMA_FULLPATH)));
        default:
            throw new RuntimeException("Unknown cache key");
        }
        
    }

    private JsonNode getQualitiesFromSchema(JsonNode schema) {
        TF2Schema tf2Schema = new TF2Schema(schema);
        Map<Integer, TF2SchemaQuality> qualityMap = tf2Schema.getCompleteQualityMap();
        JsonNode node = jsonMapper.valueToTree(qualityMap);
        return node;
    }

    private JsonNode getItemsFromSchema(JsonNode schema) {
        TF2Schema tf2Schema = new TF2Schema(schema);
        List<TF2SchemaItem> qualityMap = tf2Schema.getItemsWithNameAndDefIndexAndImage();
        JsonNode node = jsonMapper.valueToTree(qualityMap);
        return node;
    }

    private void cloneRepo() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
        // clone repository
        File localPath = new File(directory);
        Git git = null;
        try {
            git = Git.cloneRepository().setURI(credentials.getRemoteUrl()).setDirectory(localPath).setBranch(MASTER).call();
        } finally {
            if (git != null) {
                git.close();
            }
        }
    }

    private void saveSchemaContents(JsonNode node) throws JsonGenerationException, JsonMappingException, IOException {
        // save the schema contents to disk
        jsonMapper.writeValue(new File(directory + TF2_SCHEMA_FULLPATH), node);
    }

    private void cacheInvalidate() {
        cache.invalidate(CACHE_SCHEMA_KEY);
        cache.invalidate(CACHE_ITEMS_KEY);
        cache.invalidate(CACHE_QUALITIES_KEY);
    }

    private void commitAndPush() throws CannotCommitException {
        // commit the code changes and sync
        Git git = null;
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.readEnvironment().findGitDir(new File(directory)).build();
            git = new Git(repository);
            git.commit().setMessage("Schema Update").setAll(true).call();

            Iterable<PushResult> results = git.push().setRemote(credentials.getRemoteUrl()).setCredentialsProvider(credentialsProvider).call();
            Iterator<PushResult> i = results.iterator();
            while (i.hasNext()) {
                PushResult pushResult = i.next();
                Collection<RemoteRefUpdate> updates = pushResult.getRemoteUpdates();
                for (RemoteRefUpdate r : updates) {
                    if (Status.REJECTED_NONFASTFORWARD.equals(r.getStatus())) {
                        throw new CannotCommitException();
                    }
                }
            }
            logger.info("Successfully committed schema change");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (git != null) {
                git.close();
            }
        }
    }

    public SchemaDao(String directory, Credentials credentials) throws InvalidRemoteException, TransportException, GitAPIException, IOException {
        this.directory = directory;
        this.credentials = credentials;
        this.credentialsProvider = new UsernamePasswordCredentialsProvider(credentials.getToken(), "");
        this.loader = new CacheLoader<String, JsonNode>() {
            public JsonNode load(String key) throws RuntimeException, JsonProcessingException, IOException {
                return loadDataFromDisk(key);
            }
        };
        this.cache = CacheBuilder.newBuilder().build(loader);
        if (!doesLocalRepoExist()) {
            cloneRepo();
        }
    }

    public JsonNode getItems() throws ExecutionException {
        return cache.get(CACHE_ITEMS_KEY);
    }

    public JsonNode getQualities() throws ExecutionException {
        return cache.get(CACHE_QUALITIES_KEY);
    }

}
