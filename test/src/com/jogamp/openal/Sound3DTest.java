package com.jogamp.openal;

/**
 * Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use in the
 * design, construction, operation or maintenance of any nuclear facility.
 */
import java.io.*;
import javax.sound.sampled.*;
import com.jogamp.openal.sound3d.*;

/**
 * @author Athomas Goldberg
 *
 */
public class Sound3DTest {

    public static float lerp(float v1, float v2, float t) {
        float result = 0;
        result = v1 + ((v2 - v1) * t);
        return result;
    }

    public static void main(String[] args) throws IOException, InterruptedException, UnsupportedAudioFileException {

        AudioSystem3D.init();

        // create the initial context - this can be collapsed into the init.
        Device device = AudioSystem3D.openDevice(null);
        Context context = AudioSystem3D.createContext(device);
        AudioSystem3D.makeContextCurrent(context);

        // get the listener object
        Listener listener = AudioSystem3D.getListener();
        listener.setPosition(0, 0, 0);

        // load a source and play it
        Source source1 = AudioSystem3D.loadSource(Sound3DTest.class.getResourceAsStream("lewiscarroll.wav"));
        source1.setPosition(0, 0, 0);
        source1.setLooping(true);
        source1.play();

        Thread.sleep(10000);

        // move the source
        source1.setPosition(1, 1, 1);

        // move the listener
        for (int i = 0; i < 1000; i++) {
            float t = ((float) i) / 1000f;
            float lp = lerp(0f, 2f, t);
            listener.setPosition(lp, lp, lp);
            Thread.sleep(10);
        }

        // fade listener out.
        for (int i = 0; i < 1000; i++) {
            float t = ((float) i) / 1000f;
            float lp = lerp(1f, 0f, t);
            listener.setGain(lp);
            Thread.sleep(10);
        }

        source1.stop();
        source1.delete();
        context.destroy();
        device.close();

    }
}
