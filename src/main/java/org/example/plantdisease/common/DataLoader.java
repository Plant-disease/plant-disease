package org.example.plantdisease.common;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.entity.*;
import org.example.plantdisease.enums.Permission;
import org.example.plantdisease.enums.RoleType;
import org.example.plantdisease.repository.*;
import org.example.plantdisease.service.init.InitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final InitService initService;
    private final RoleRepository roleRepository;
    private final FruitRepository fruitRepository;
    private final DiseaseRepository diseaseRepository;
    private final TreatmentRepository treatmentRepository;
    private final LeafConditionRepository leafConditionRepository;

    @Value("${spring.sql.init.mode}")
    private String initMode;

    @Override
    public void run(String... args) {

        if (initMode.equals("always")) {

            initService.createRoleByRoleType();

            fruitRepository.save(new Fruit("Tomato"));
            fruitRepository.save(new Fruit("Potato"));

            diseaseRepository.save(new Disease("SPOT"));
            diseaseRepository.save(new Disease("VTM"));

            leafConditionRepository.save(new LeafCondition("Healthy"));
            LeafCondition firstDegreeDisease = leafConditionRepository.save(new LeafCondition("First_Tomato___Bacterial_spot"));
            LeafCondition secondDegreeDisease = leafConditionRepository.save(new LeafCondition("Second_Tomato___Early_blight"));
            LeafCondition thirdDegreeDisease = leafConditionRepository.save(new LeafCondition("Third_Tomato___Late_blight"));
            LeafCondition fourthDegreeDisease = leafConditionRepository.save(new LeafCondition("Fourth_Tomato___Leaf_Mold"));
            LeafCondition fifthDegreeDisease = leafConditionRepository.save(new LeafCondition("Fifth_Tomato___Septoria_leaf_spot"));
            LeafCondition sixthDegreeDisease = leafConditionRepository.save(new LeafCondition("Sixth_Tomato___Spider_mites Two-spotted_spider_mite"));
            LeafCondition seventhDegreeDisease = leafConditionRepository.save(new LeafCondition("Seventh_Tomato___Target_Spot"));
            LeafCondition eighthDegreeDisease = leafConditionRepository.save(new LeafCondition("Eighth_Tomato___Tomato_mosaic_virus"));
            LeafCondition ninthDegreeDisease = leafConditionRepository.save(new LeafCondition("Ninth_Tomato___Tomato_Yellow_Leaf_Curl_Virus"));

            treatmentRepository.save(new Treatment("First_Tomato___Bacterial_spot instruction", firstDegreeDisease));
            treatmentRepository.save(new Treatment("Second_Tomato___Early_blight instruction", secondDegreeDisease));
            treatmentRepository.save(new Treatment("Third_Tomato___Late_blight instruction", thirdDegreeDisease));
            treatmentRepository.save(new Treatment("Fourth_Tomato___Leaf_Mold instruction", fourthDegreeDisease));
            treatmentRepository.save(new Treatment("Fifth_Tomato___Septoria_leaf_spot instruction", fifthDegreeDisease));
            treatmentRepository.save(new Treatment("Sixth_Tomato___Spider_mites Two-spotted_spider_mite instruction", sixthDegreeDisease));
            treatmentRepository.save(new Treatment("Seventh_Tomato___Target_Spot instruction", seventhDegreeDisease));
            treatmentRepository.save(new Treatment("Eighth_Tomato___Tomato_mosaic_virus instruction", eighthDegreeDisease));
            treatmentRepository.save(new Treatment("Ninth_Tomato___Tomato_Yellow_Leaf_Curl_Virus instruction", ninthDegreeDisease));
        }
        else if (initMode.equals("never")) {
            List<Role> roleList = new ArrayList<>();
            for (RoleType type : RoleType.values()) {
                    Set<Permission> permissionSet = Stream.of(Permission.values()).filter(permission -> permission.getRoleTypes().contains(type)).collect(Collectors.toSet());
                    Role role = roleRepository.findByType(type).orElse(new Role());
                    role.setName(type.name());
                    role.setType(type);
                    role.setPermissions(permissionSet);
                    roleList.add(role);
            }
            roleRepository.saveAll(roleList);
        }
    }
}
