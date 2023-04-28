package ibf2022.assessment.paf.batch3.repositories;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderRepository {

    private final MongoTemplate mongoTemplate;
    public void insertOrder(String orderJson) {
        DBObject dbObject = BasicDBObject.parse(orderJson);
        mongoTemplate.insert(dbObject, "orders");
    }

}
