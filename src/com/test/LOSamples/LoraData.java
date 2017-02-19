/*
 * Copyright (C) 2016 Orange
 *
 * This software is distributed under the terms and conditions of the 'BSD-3-Clause'
 * license which can be found in the file 'LICENSE.txt' in this package distribution
 * or at 'https://opensource.org/licenses/BSD-3-Clause'.
 */
package com.test.LOSamples;

import java.util.List;

/**
 * Structure of a "data message" that can be sent by a LoRa device into Live Objects.
 */
public class LoraData {

    /**
     * Stream identifier: urn:lora:<devEUI>!uplink
     */
    public String streamId;

    /**
     * timestamp (ISO8601 format)
     */
    public String timestamp;

    /**
     * Data "model" of the field "value"
     */
    public String model;

    /**
     * Value
     */
    public LoraDataValue value;

    /**
     * Tags
     */
    public List<String> tags;

    /*
    * Metadata
     */
    public LoraMetadata metadata;

    @Override
    public String toString() {
        return "LoraData{" +
                "streamId='" + streamId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", model='" + model + '\'' +
                ", value=" + value +
                ", tags=" + tags +
                ", metadata=" + metadata +
                '}';
    }


    // *********************
    // *** INNER CLASSES ***
    // *********************

    public class LoraDataValue {

        /**
         * Port of the device on which the command was sent
         */
        public Integer port;

        /**
         * Downlink frame counter of the command
         */
        public Integer fcnt;

        /**
         * Signal quality indicator from 1 to 5
         */
        public Integer signalLevel;

        /**
         * Hexadecimal raw data of the message
         */
        public String payload;

        @Override
        public String toString() {
            return "LoraDataValue{" +
                    "port=" + port +
                    ", fcnt=" + fcnt +
                    ", signalLevel=" + signalLevel +
                    ", payload='" + payload + '\'' +
                    '}';
        }

    }

    public class LoraMetadata {

        /**
         * Source of the payload: urn:lora:<devEUI>
         */
        public String source;

        @Override
        public String toString() {
            return "LoraMetadata{" +
                    "source='" + source + '\'' +
                    '}';
        }

    }

}
