package wenavi.jp.nal.loginlibrary.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;

/**
 * Copyright © Nals
 * Created by TrangLT on 21/03/18.
 */
@AllArgsConstructor
@Data
@Builder
public class ApiError {
    private int statusCode;
    private String message;
}
