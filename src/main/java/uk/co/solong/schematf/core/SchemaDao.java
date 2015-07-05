package uk.co.solong.schematf.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class SchemaDao {
    private final ObjectMapper m = new ObjectMapper();
    private final CacheLoader<String, JsonNode> loader;
    private final LoadingCache<String, JsonNode> cache;
    private static final Logger logger = LoggerFactory.getLogger(SchemaDao.class);
    private final String directory;
    private final Credentials credentials;
    private final CredentialsProvider credentialsProvider;
    private static final String CACHE_KEY = "latest";

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
        // get the latest schema from a cache. cacheloader will pull the latest
        // from github if necessary
        return cache.get(CACHE_KEY);
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
            git.fetch().setRemote("origin").call();
            git.reset().setMode(ResetType.HARD).setRef("origin/master").call();
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

    private JsonNode loadSchemaFromDisk() throws JsonProcessingException, IOException {
        // load the schema from disk
        return m.readTree(new File(directory + "/schema.json"));
    }

    private void cloneRepo() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
        // clone repository
        File localPath = new File(directory);
        Git git = null;
        try {
            git = Git.cloneRepository().setURI(credentials.getRemoteUrl()).setDirectory(localPath).setBranch("master").call();
        } finally {
            if (git != null) {
                git.close();
            }
        }
    }

    private void saveSchemaContents(JsonNode node) throws JsonGenerationException, JsonMappingException, IOException {
        // save the schema contents to disk
        m.writeValue(new File(directory + "/schema.json"), node);
    }

    private void cacheInvalidate() {
        cache.invalidate(CACHE_KEY);
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
                return loadSchemaFromDisk();
            }
        };
        this.cache = CacheBuilder.newBuilder().build(loader);
        if (!doesLocalRepoExist()) {
            cloneRepo();
        }
    }

}
