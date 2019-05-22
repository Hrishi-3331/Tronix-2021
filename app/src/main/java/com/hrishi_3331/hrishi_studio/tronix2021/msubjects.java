package com.hrishi_3331.hrishi_studio.tronix2021;

public class msubjects {
    private String name;
    private String code;

    public msubjects() {

    }

    public msubjects(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {

        switch (name){
            case "Maths":
                return "MAL";

            case "Analog Circuit Design":
                return "ACD";

            case "Electromagnetic Fields":
                return "EMF";

            case "Microprocessors and Interfacing":
                return "MCP";

            case "Signals and Systems":
                return "SNS";

            default:
                return null;
        }


    }
}
