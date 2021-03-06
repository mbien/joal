/*
 * Created on Saturday, July 10 2010 17:08
 */
package com.jogamp.openal.impl;

import com.jogamp.openal.ALException;
import com.jogamp.openal.ALCdevice;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * ALC implementation.
 * @author Michael Bien
 */
public class ALCImpl extends ALCAbstractImpl {

    public String alcGetString(ALCdevice device, int param) {
        if (device == null && param == ALC_DEVICE_SPECIFIER) {
            throw new ALException("Call alcGetDeviceSpecifiers to fetch all available device names");
        }

        ByteBuffer buf = alcGetStringImpl(device, param);
        if (buf == null) {
            return null;
        }
        byte[] res = new byte[buf.capacity()];
        buf.get(res);
        try {
            return new String(res, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new ALException(e);
        }
    }

    /**
     * Fetches the names of the available ALC device specifiers.
     * Equivalent to the C call alcGetString(NULL, ALC_DEVICE_SPECIFIER).
     */
    public String[] alcGetDeviceSpecifiers() {
        return getDoubleNullTerminatedString(ALC_DEVICE_SPECIFIER);
    }

    /**
     * Fetches the names of the available ALC capture device specifiers.
     * Equivalent to the C call alcGetString(NULL, ALC_CAPTURE_DEVICE_SPECIFIER).
     */
    public String[] alcGetCaptureDeviceSpecifiers() {
        return getDoubleNullTerminatedString(ALC_CAPTURE_DEVICE_SPECIFIER);
    }

    private String[] getDoubleNullTerminatedString(int which) {
        ByteBuffer buf = alcGetStringImpl(null, which);
        if (buf == null) {
            return null;
        }
        byte[] bytes = new byte[buf.capacity()];
        buf.get(bytes);
        try {
            ArrayList/*<String>*/ res = new ArrayList/*<String>*/();
            int i = 0;
            while (i < bytes.length) {
                int startIndex = i;
                while ((i < bytes.length) && (bytes[i] != 0)) {
                    i++;
                }
                res.add(new String(bytes, startIndex, i - startIndex, "US-ASCII"));
                i++;
            }
            return (String[]) res.toArray(new String[res.size()]);
        } catch (UnsupportedEncodingException e) {
            throw new ALException(e);
        }
    }
}
