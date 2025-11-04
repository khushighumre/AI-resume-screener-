

package com.resumescreener.extractor;

import com.resumescreener.model.Candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.Period;
import java.util.Locale;

public class DataExtractor {

    public Candidate extract(String text) {
        if (text == null || text.trim().isEmpty()) {
            return createDefaultCandidate();
        }

        Candidate candidate = new Candidate();

        // Extract Name
        candidate.setName(extractName(text));

        // Extract Skills
        candidate.setSkills(extractSkills(text));

        // Extract Experience
        candidate.setExperience(extractExperience(text));

        return candidate;
    }

    private String extractName(String text) {
        String[] namePatterns = {
            "Name[:\\s]+([A-Z][a-z]+\\s+[A-Z][a-z]+)",  
            "([A-Z][a-z]+\\s+[A-Z][a-z]+(?:\\s+[A-Z][a-z]+)?)", 
            "^([A-Z][A-Z\\s]+)$"  
        };

        for (String patternStr : namePatterns) {
            Pattern pattern = Pattern.compile(patternStr, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                String name = matcher.group(1).trim();
                if (name.length() > 2 && name.length() < 50) {
                    return name;
                }
            }
        }
        return "Unknown";
    }

    private List<String> extractSkills(String text) {
        List<String> skills = new ArrayList<>();
        String textLower = text.toLowerCase();

        String[] skillKeywords = {
            "Java", "Python", "JavaScript", "C++", "C#", "PHP", "Ruby", "Go", "Kotlin", "Swift",
            "HTML", "CSS", "React", "Angular", "Vue", "Node.js", "Express",
            "Spring", "Spring Boot", "Django", "Flask", "Laravel", "Rails",
            "SQL", "MySQL", "PostgreSQL", "MongoDB", "Oracle", "SQLite",
            "AWS", "Azure", "GCP", "Docker", "Kubernetes", "Jenkins",
            "Git", "GitHub", "GitLab", "Jira", "Agile", "Scrum",
            "Machine Learning", "AI", "Data Science", "TensorFlow", "PyTorch",
            "REST", "API", "Microservices", "DevOps", "CI/CD"
        };

        for (String skill : skillKeywords) {
            if (textLower.contains(skill.toLowerCase())) {
                skills.add(skill);
            }
        }

        return skills;
    }

    private int extractExperience(String text) {
        int years = extractExplicitYears(text);
        if (years > 0) return years;

        years = extractExperienceFromDateRanges(text);
        if (years > 0) return years;
        
      
        return extractBasicExperience(text);
    }
    
    private int extractBasicExperience(String text) {
        String textLower = text.toLowerCase();
        
       
        String[] workKeywords = {
            "intern", "internship", "trainee", "apprentice",
            "developer", "engineer", "analyst", "consultant",
            "worked at", "employed at", "experience at"
        };
        
        for (String keyword : workKeywords) {
            if (textLower.contains(keyword)) {
                return 1; 
            }
        }
        
        return 0;
    }

    private int extractExplicitYears(String text) {
        String[] expPatterns = {
            "(\\d+)\\s*(?:years?|yrs?)\\s*(?:of\\s*)?(?:experience|exp)",
            "(?:experience|exp)[:\\s]*(\\d+)\\s*(?:years?|yrs?)",
            "(\\d+)\\+?\\s*(?:years?|yrs?)",
            "(?:over|more than)\\s*(\\d+)\\s*(?:years?|yrs?)"
        };

        for (String patternStr : expPatterns) {
            Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                try {
                    int years = Integer.parseInt(matcher.group(1));
                    if (years >= 0 && years <= 50) {
                        return years;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        return 0;
    }


    private int extractExperienceFromDateRanges(String text) {
        Pattern dateRangePattern = Pattern.compile(
            "(\\b(?:Jan|Feb|Mar|Apr|May|Jun|June|Jul|July|Aug|Sep|Sept|Oct|Nov|Dec)[a-z]*\\s+\\d{4})" +  
            "\\s*(?:–|-|to|\\s)\\s*" +  
            "(\\b(?:Present|Now|[A-Z][a-z]+\\s+\\d{4}))",  
            Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = dateRangePattern.matcher(text);
        int totalMonths = 0;

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMM yyyy")
                .toFormatter(Locale.ENGLISH);

        while (matcher.find()) {
            String startStr = matcher.group(1);
            String endStr = matcher.group(2);

            try {
                YearMonth start = YearMonth.parse(startStr, formatter);
                YearMonth end;

                if (endStr.equalsIgnoreCase("Present") || endStr.equalsIgnoreCase("Now")) {
                    end = YearMonth.now();
                } else {
                    end = YearMonth.parse(endStr, formatter);
                }

                totalMonths += Period.between(start.atDay(1), end.atEndOfMonth()).toTotalMonths();
            } catch (Exception ignored) {}
        }

        if (totalMonths > 0) {
            return Math.max(1, totalMonths / 12);  // round to full years
        }

        return 0;
    }

    private Candidate createDefaultCandidate() {
        Candidate candidate = new Candidate();
        candidate.setName("Unknown");
        candidate.setSkills(new ArrayList<>());
        candidate.setExperience(0);
        return candidate;
    }
}
