package com.resumescreener.model;

import java.util.List;

public class Candidate extends Person {
    private List<String> skills;
    private int experience;

    public Candidate() {}

    public Candidate(String name, List<String> skills, int experience) {
        super(name);
        this.skills = skills;
        this.experience = experience;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
