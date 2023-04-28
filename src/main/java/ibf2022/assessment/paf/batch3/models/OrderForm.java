package ibf2022.assessment.paf.batch3.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrderForm {

    private String orderId;

    private LocalDateTime date;

    private int breweryId;

    private List<Order> orders;

}