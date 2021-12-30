package com.example.gecktrack;

public class GeckoModel
{

// PRIVATE INSTANCE VARIABLES ----------------------------------------------------------------------


    private  int    geckoID;
    private  String name;
    private  int    geckoSpecies;  // foreign key, how will this work?
    private  char   sex;
    private  String birthday;
    private  int    age;
    private  double weight;
    private  String morph;
    private  int    temperature;
    private  int    humidity;
    private  String purchasedFrom;
    private  String photoID;


// CONSTRUCTOR -------------------------------------------------------------------------------------


    public GeckoModel(int geckoID, String name, int geckoSpecies, char sex, String birthday,
                      int age, double weight, String morph, int temperature, int humidity,
                      String purchasedFrom, String photoID)
    {
        this.geckoID       = geckoID;
        this.name          = name;
        this.geckoSpecies  = geckoSpecies;
        this.sex           = sex;
        this.birthday      = birthday;
        this.age           = age;
        this.weight        = weight;
        this.morph         = morph;
        this.temperature   = temperature;
        this.humidity      = humidity;
        this.purchasedFrom = purchasedFrom;
        this.photoID       = photoID;
    }


// GETTERS AND SETTERS -----------------------------------------------------------------------------


    public int getGeckoID()
    {
        return geckoID;
    }

    public void setGeckoID(int geckoId)
    {
        this.geckoID = geckoId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getGeckoSpecies()
    {
        return geckoSpecies;
    }

    public void setGeckoSpecies(int geckoSpecies)
    {
        this.geckoSpecies = geckoSpecies;
    }

    public char getSex()
    {
        return sex;
    }

    public void setSex(char sex)
    {
        this.sex = sex;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public String getMorph()
    {
        return morph;
    }

    public void setMorph(String morph)
    {
        this.morph = morph;
    }

    public int getTemperature()
    {
        return temperature;
    }

    public void setTemperature(int temperature)
    {
        this.temperature = temperature;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public void setHumidity(int humidity)
    {
        this.humidity = humidity;
    }

    public String getPurchasedFrom()
    {
        return purchasedFrom;
    }

    public void setPurchasedFrom(String purchasedFrom)
    {
        this.purchasedFrom = purchasedFrom;
    }

    public String getPhotoID()
    {
        return photoID;
    }

    public void setPhotoID(String photoID)
    {
        this.photoID = photoID;
    }


// TOSTRING ----------------------------------------------------------------------------------------


    @Override
    public String toString()
    {
        return "GeckoModel\n" +
                "{\n" +
                "  geckoID = " + geckoID + "\n" +
                "  name = '" + name + '\'' + "\n" +
                "  geckoSpecies = " + geckoSpecies + "\n" +
                "  sex = " + sex + "\n" +
                "  birthday = '" + birthday + '\'' + "\n" +
                "  age = " + age + "\n" +
                "  morph = '" + morph + '\'' + "\n" +
                "  temperature = " + temperature + "\n" +
                "  humidity = " + humidity + "\n" +
                "  purchasedFrom = '" + purchasedFrom + '\'' + "\n" +
                "  photoID = '" + photoID + '\'' + "\n" +
                "}\n\n";
    }
}
