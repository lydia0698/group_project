package test;

import data_access.FoodKeeperDataAccessObject;
import entity.StorageCondition;

public class FoodKeeperTest {

    public static void main(String[] args) {
        System.out.println(
                "Working directory: " + System.getProperty("user.dir")
        );

        FoodKeeperDataAccessObject foodKeeper =
                new FoodKeeperDataAccessObject();

        System.out.println("Butter refrigerated: "
                + foodKeeper.getShelfLifeDays(
                "Butter",
                StorageCondition.REFRIGERATION));

        System.out.println("Tomato frozen: "
                + foodKeeper.getShelfLifeDays(
                "Tomato",
                StorageCondition.FREEZING));

        System.out.println("Tomato refrigerated: "
                + foodKeeper.getShelfLifeDays(
                "Tomato",
                StorageCondition.REFRIGERATION));

        System.out.println(
                foodKeeper.getShelfLifeEstimate(
                        "Butter",
                        StorageCondition.REFRIGERATION
                ).get().getDays()
        );

        System.out.println(
                foodKeeper.getShelfLifeEstimate(
                        "Butter",
                        StorageCondition.REFRIGERATION
                ).get().getDateBasis()
        );

    }
}

