package hyeon.buddy.domain;

import hyeon.buddy.dto.CategoryRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    public static Category from(CategoryRequestDTO dto){
        return Category.builder()
                .title(dto.getTitle())
                .build();

    }
}
