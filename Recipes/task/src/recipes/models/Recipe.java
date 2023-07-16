package recipes.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String description;
    private String author;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients")
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_directions")
    private List<String> directions;

    private LocalDateTime date;
}
