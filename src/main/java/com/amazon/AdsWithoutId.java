package com.amazon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdsWithoutId {
    private String name;
    private String description;
    private String fileId;
}
