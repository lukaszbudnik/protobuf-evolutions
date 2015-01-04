package io.github.lukaszbudnik.protobuf.model;

import com.github.lukaszbudnik.protobuf.model.Model;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class SerialiseDeserialiseTest {

    @Test
    public void shouldSerialiseDeserialise() throws InvalidProtocolBufferException {
        Model.TestCaseV1.Builder tcBuilder = Model.TestCaseV1.newBuilder();
        byte[] bytes = new byte[1024];
        new Random().nextBytes(bytes);
        ByteString content = ByteString.copyFrom(bytes);
        Model.TestCaseV1 tc = tcBuilder.setName("test case v1").setContent(content).build();

        Model.TestRunV1.Builder trBuilder = Model.TestRunV1.newBuilder();
        Model.TestRunV1 tr = trBuilder.setName("test run v1").setConcurrencyLevel(2121212121).addTestCases(tc).build();

        byte[] serialised = tr.toByteArray();

        System.out.println(serialised.length);

        Model.TestRunV2 deserialisedTr = Model.TestRunV2.parseFrom(serialised);

        Assert.assertEquals(tr.getName(), deserialisedTr.getName());
        // timezone does not exists in TestRunV1
        // it's optional but Protobuf does not have a concept of nulls
        Assert.assertTrue(deserialisedTr.getTimezone().isEmpty());
        Assert.assertEquals(tc, deserialisedTr.getTestCases(0));
    }

}
