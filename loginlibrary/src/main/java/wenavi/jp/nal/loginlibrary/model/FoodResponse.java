package wenavi.jp.nal.loginlibrary.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Copyright © Nals
 * Created by TrangLT on 1/7/19.
 */
@Data
@AllArgsConstructor
public class FoodResponse {
    private List<Food> foods;
}
