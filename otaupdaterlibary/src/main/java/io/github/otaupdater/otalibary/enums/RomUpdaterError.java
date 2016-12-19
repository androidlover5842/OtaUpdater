package io.github.otaupdater.otalibary.enums;

public enum RomUpdaterError {
    /**
     * Google Play returned "Varies by device"
     */
    UPDATE_VARIES_BY_DEVICE,

    /**
     * No Internet connection available
     */
    NETWORK_NOT_AVAILABLE,

    /**
     * URL for the XML file is not valid
     */
    XML_URL_MALFORMED,

    /**
     * XML file is invalid or is down
     */
    XML_ERROR,
    /**
     * URL for the JSON file is not valid
     */
    JSON_URL_MALFORMED,

    /**
     * JSON file is invalid or is down
     */
    JSON_ERROR


    }
