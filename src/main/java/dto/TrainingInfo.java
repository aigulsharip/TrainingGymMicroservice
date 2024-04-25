package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingInfo implements Serializable {
    private String traineeName;
    private String trainerName;
    private String trainingType;
    private int trainingDuration;
}
