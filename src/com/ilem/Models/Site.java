package com.ilem.Models;


import java.util.ArrayList;

/**
 * Created by laassiri on 23/03/15.
 */
public class Site {

    public Site(String nomDomaine, String ip, int port, String msgError, boolean https, boolean mode, ArrayList<String> wlList) {
        this.nomDomaine = nomDomaine;
        this.ip = ip;
        this.port = port;
        this.https = https;
        this.mode = mode;
        this.msgError = msgError;
        this.wlList= wlList;
    }

    public Site() {
    }

    public Site(String nomDomaine) {
        this.nomDomaine = nomDomaine;
        this.ip = "0.0.0.0";
        this.port = 1111;
        this.https = false;
        this.mode = false;
        this.msgError = "error";
        this.wlList=new ArrayList<>();
    }

    private String nomDomaine;

    private String ip;

    private int port;

    private boolean https;

    private boolean mode;

    private String msgError ;

    private ArrayList<String> wlList;

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getNomDomaine() {
        return nomDomaine;
    }

    public void setNomDomaine(String nomDomaine) {
        this.nomDomaine = nomDomaine;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public ArrayList<String> getWlList() {
        return wlList;
    }

    public void setWlList(ArrayList<String> wlList) {
        this.wlList = wlList;
    }


    @Override
    public String toString() {
        return new StringBuffer(" nom de domaine : ").append(this.nomDomaine)
                .append(" ip : ").append(this.ip).append(" port : ").append(this.port).append("msg d'erreur : ").append(this.msgError).append(" https : ").append(this.https).append(" mode : ").append(this.mode)
                .toString();
    }
}
