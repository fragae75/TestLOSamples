/*
 * Copyright (C) 2016 Orange
 *
 * This software is distributed under the terms and conditions of the 'BSD-3-Clause'
 * license which can be found in the file 'LICENSE.txt' in this package distribution
 * or at 'https://opensource.org/licenses/BSD-3-Clause'.
 */
package com.test.LOSamples;


import java.util.List;
import java.util.Map;

/**
 * Structure of a "data message" that can be sent by a device into Live Objects.
 */
public class DeviceData {

    /**
     * Stream identifier
     */
    public String s;

    /**
     * timestamp (ISO8601 format)
     */
    public String ts;

    /**
     * Data "model"
     */
    public String m;

    /**
     * Value
     */
    public Map<String, Object> v;

    /**
     * Tags
     */
    public List<String> t;

    /**
     * Location [latitude, longitude]
     */
    public Double[] loc;

}
