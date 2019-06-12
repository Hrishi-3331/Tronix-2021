package com.hrishi_3331.hrishi_studio.tronix2021;

import java.util.Arrays;

public class MessageUIDgenerator {

    private String uid1;
    private String uid2;
    private String Muuid;

    public MessageUIDgenerator(String uid1, String uid2) {
        this.uid1 = uid1;
        this.uid2 = uid2;
    }

    public void generateMessageId(){
        String ids[] = new String[]{uid1, uid2};
        Arrays.sort(ids);
        Muuid = ids[0] + ids[1];
    }

    public String getMessageId() {
        return Muuid;
    }
}
