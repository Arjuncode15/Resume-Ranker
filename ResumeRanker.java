import java.io.*;
import java.util.*;

class ResumeScore {
    String filename;
    int score;

    ResumeScore(String filename, int score) {
        this.filename = filename;
        this.score = score;
    }
}

public class ResumeRanker {

    public static List<String> getKeywords(String filePath) throws Exception {
        List<String> keywords = new ArrayList<>();
        Scanner sc = new Scanner(new File(filePath));
        while (sc.hasNext()) {
            String word = sc.next().toLowerCase().replaceAll("[^a-z]", "");
            if (!word.isBlank()) {
                keywords.add(word);
            }
        }
        sc.close();
        return keywords;
    }

    public static int scoreResume(File resume, Set<String> keywordSet) throws Exception {
        int score = 0;
        Scanner sc = new Scanner(resume);
        while (sc.hasNext()) {
            String word = sc.next().toLowerCase().replaceAll("[^a-z]", "");
            if (keywordSet.contains(word)) {
                score++;
            }
        }
        sc.close();
        return score;
    }

    public static void main(String[] args) throws Exception {
        String resumeFolder = "resumes";
        String jobDescFile = "JobDescription.txt";

        List<String> keywords = getKeywords(jobDescFile);
        Set<String> keywordSet = new HashSet<>(keywords);

        File folder = new File(resumeFolder);
        File[] resumeFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        List<ResumeScore> results = new ArrayList<>();

        if (resumeFiles == null) {
            System.out.println("No resumes found.");
            return;
        }

        for (File resume : resumeFiles) {
            int score = scoreResume(resume, keywordSet);
            results.add(new ResumeScore(resume.getName(), score));
        }

        // Sort descending by score
        results.sort((a, b) -> b.score - a.score);

        System.out.println("\n Resume Rankings:");
        for (ResumeScore rs : results) {
            System.out.println(rs.filename + " Score: " + rs.score);
        }
    }
}
