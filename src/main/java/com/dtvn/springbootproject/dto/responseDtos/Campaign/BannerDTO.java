package     com.dtvn.springbootproject.dto.responsedtos.Campaign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO {
    private int campaignId;
    private String urlImg;
}
