package blog.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogQuery {

    private String title;
    private Long typeId;
    private boolean recommend;
}
