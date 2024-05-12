package org.example.plantdisease.service;



import org.example.plantdisease.entity.Attachment;
import org.example.plantdisease.entity.LeafData;
import org.example.plantdisease.entity.LeafMeasurement;

import java.util.List;

public interface LeafMeasurementService {

    List<LeafMeasurement> getLeafMeasurementToSaveDB(LeafData leafData, List<Attachment> attachmentList);

    List<LeafMeasurement> getLeafMeasurementToCheckKNN(List<Attachment> attachmentList);
}
