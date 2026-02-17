package ucas.recipebook.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Recipe implements Serializable {
    private String id;
    private String title;
    private List<String> ingredients;
    private List<String> steps;
    private String category;
    private String videoUrl;
    private String imageUrl;
    private String creatorId;
    private Date createdAt;

    public Recipe() {
        // Required empty constructor for Firestore
    }

    public Recipe(String id, String title, List<String> ingredients, List<String> steps,
                  String category, String videoUrl, String imageUrl, String creatorId, Date createdAt) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.category = category;
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}


