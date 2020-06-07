package uk.co.solong.schematf.schemadao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemaEntry {
    private final Long publicationTime;
    private final String file;
    private final Long discoveryTime;
    private final Source source;
    private final boolean publicationTimeAccurate;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SchemaEntry(@JsonProperty("publicationTime") Long publicationTime,
                       @JsonProperty("file") String file,
                       @JsonProperty("discoveryTime") Long discoveryTime,
                       @JsonProperty("source") Source source,
                       @JsonProperty("publicationTimeAccurate") boolean publicationTimeAccurate) {
        this.publicationTime = publicationTime;
        this.file = file;
        this.discoveryTime = discoveryTime;
        this.source = source;
        this.publicationTimeAccurate = publicationTimeAccurate;
    }

    /**
     * The schema publication time. This is the value used for http 304 not modified requests.
     *
     * @return
     */
    public Long getPublicationTime() {
        return publicationTime;
    }

    /**
     * The name of the file as retrievable by an s3 bucket or similar.
     *
     * @return
     */
    public String getFile() {
        return file;
    }

    /**
     * The exact time this file was discovered. The publication time may be 1pm but we may discover it at 1.01pm.
     * For external sources this should be the time that we added it to the database.
     *
     * @return
     */
    public Long getDiscoveryTime() {
        return discoveryTime;
    }

    /**
     * The source that discovered this change. For example, if this was sourced by schematf itself or an external source.
     *
     * @return
     */
    public Source getSource() {
        return source;
    }

    /**
     * State if the publication time is accurate or invented. For external sources we may not know the publication time.
     *
     * @return
     */
    public boolean isPublicationTimeAccurate() {
        return publicationTimeAccurate;
    }
}
