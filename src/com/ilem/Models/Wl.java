package com.ilem.Models;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

import java.util.ArrayList;

/**
 * Created by laassiri on 15/04/15.
 */
public class Wl{
        private String id;
        private String desc;
        public String getId() {
            return id;
        }

        public Wl() {
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
