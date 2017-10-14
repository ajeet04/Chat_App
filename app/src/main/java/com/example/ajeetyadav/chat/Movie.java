package com.example.ajeetyadav.chat;

/**
 * Created by Ajeet Yadav on 9/18/2017.
 */





    public class Movie {
        public  String name;
        public  int image;



    public Movie(String name, int image) {
            this.name = name;
            this.image = image;

        }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

        public void setImage(int image) {
            this.image =image;
        }

        public int getImage() {
            return image;
        }


    }
