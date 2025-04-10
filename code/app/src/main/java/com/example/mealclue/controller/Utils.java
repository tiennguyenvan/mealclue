package com.example.mealclue.controller;

import android.database.sqlite.SQLiteDatabase;

public class Utils {
    public enum RegionType {
        COUNTRY,
        DISTRICT,
        PROVINCE
    }

    public static void demoDataCreation(SQLiteDatabase db) {
        db.execSQL("INSERT INTO android_metadata VALUES('en_US');");
        db.execSQL("INSERT INTO User VALUES(5,'','timvan',0,'V6B 1A1','timvan','2987074');");
        db.execSQL("INSERT INTO User VALUES(6,NULL,'snowvan',0,'V6B 3H7','snowvan','2987074');");
        db.execSQL("INSERT INTO User VALUES(7,NULL,'tedbc',0,'V7Y 1K8','tedbc','2987074');");
        db.execSQL("INSERT INTO User VALUES(8,NULL,'jimcanada',0,'M5V 2T6','jimcanada','2987074');");
        db.execSQL("INSERT INTO Recipe VALUES(57810,'Choucroute Garni','https://img.spoonacular.com/recipes/57810-312x231.jpg',NULL,NULL,NULL,'potato');");
        db.execSQL("INSERT INTO Recipe VALUES(157093,'Puff Paste quiche','https://img.spoonacular.com/recipes/157093-312x231.jpg',NULL,NULL,NULL,'miso');");
        db.execSQL("INSERT INTO Recipe VALUES(157111,'Vegan Baked Apples with Oat Crumble','https://img.spoonacular.com/recipes/157111-312x231.jpg',NULL,NULL,NULL,'oat');");
        db.execSQL("INSERT INTO Recipe VALUES(629963,'chilli chicken','https://img.spoonacular.com/recipes/629963-312x231.jpg',NULL,NULL,NULL,'chicken');");
        db.execSQL("INSERT INTO Recipe VALUES(631759,'Simit (Turkish Circular Bread)','https://img.spoonacular.com/recipes/631759-312x231.jpg',NULL,NULL,NULL,'bread');");
        db.execSQL("INSERT INTO Recipe VALUES(633352,'Bacon Wrapped Tofu Tacos','https://img.spoonacular.com/recipes/633352-312x231.jpg',NULL,NULL,NULL,'tofu');");
        db.execSQL("INSERT INTO Recipe VALUES(634726,'Beer - Batter Fried Shrimp','https://img.spoonacular.com/recipes/634726-312x231.jpg',NULL,NULL,NULL,'beer');");
        db.execSQL("INSERT INTO Recipe VALUES(634753,'Beer Can Chicken, Country Style Vegetables with Roasted Garlic','https://img.spoonacular.com/recipes/634753-312x231.jpg',NULL,NULL,NULL,'beer');");
        db.execSQL("INSERT INTO Recipe VALUES(635675,'Boozy Bbq Chicken','https://img.spoonacular.com/recipes/635675-312x231.jpg',NULL,NULL,NULL,'chicken');");
        db.execSQL("INSERT INTO Recipe VALUES(636177,'Broccoli Cheddar Soup','https://img.spoonacular.com/recipes/636177-312x231.jpg',NULL,NULL,NULL,'potato');");
        db.execSQL("INSERT INTO Recipe VALUES(636676,'Cacao chia pudding with avocado mousse','https://img.spoonacular.com/recipes/636676-312x231.jpg',NULL,NULL,NULL,'chia');");
        db.execSQL("INSERT INTO Recipe VALUES(637178,'Carrot Cake Cookie Sandwiches','https://img.spoonacular.com/recipes/637178-312x231.jpg',NULL,NULL,NULL,'oat');");
        db.execSQL("INSERT INTO Recipe VALUES(637832,'Chia Seed Pudding','https://img.spoonacular.com/recipes/637832-312x231.png',NULL,NULL,NULL,'chia');");
        db.execSQL("INSERT INTO Recipe VALUES(638825,'CHOCOLATE BANANA MORNING BUZZ SMOOTHIE','https://img.spoonacular.com/recipes/638825-312x231.jpg',NULL,NULL,NULL,'smoothie');");
        db.execSQL("INSERT INTO Recipe VALUES(639658,'Claypot Fish','https://img.spoonacular.com/recipes/639658-312x231.jpg',NULL,NULL,NULL,'tofu');");
        db.execSQL("INSERT INTO Recipe VALUES(641836,'Easy Baked Parmesan Chicken','https://img.spoonacular.com/recipes/641836-312x231.jpg',NULL,NULL,NULL,'chicken');");
        db.execSQL("INSERT INTO Recipe VALUES(642264,'Eggless Ginger & Mango Bread','https://img.spoonacular.com/recipes/642264-312x231.jpg',NULL,NULL,NULL,'bread');");
        db.execSQL("INSERT INTO Recipe VALUES(644627,'Ginger Sesame Dressing','https://img.spoonacular.com/recipes/644627-312x231.jpg',NULL,NULL,NULL,'rice');");
        db.execSQL("INSERT INTO Recipe VALUES(645187,'Grape and Rosemary Focaccia','https://img.spoonacular.com/recipes/645187-312x231.jpg',NULL,NULL,NULL,'bread');");
        db.execSQL("INSERT INTO Recipe VALUES(645673,'Grilled Chicken With Spinach-Chive Pesto','https://img.spoonacular.com/recipes/645673-312x231.jpg',NULL,NULL,NULL,'chicken');");
        db.execSQL("INSERT INTO Recipe VALUES(645722,'Grilled Ginger Miso Chicken','https://img.spoonacular.com/recipes/645722-312x231.jpg',NULL,NULL,NULL,'miso');");
        db.execSQL("INSERT INTO Recipe VALUES(646512,'Salmon Caesar Salad','https://img.spoonacular.com/recipes/646512-312x231.jpg',NULL,NULL,NULL,'salmon');");
        db.execSQL("INSERT INTO Recipe VALUES(646941,'Homemade Chewy Granola Bars','https://img.spoonacular.com/recipes/646941-312x231.jpg',NULL,NULL,NULL,'oat');");
        db.execSQL("INSERT INTO Recipe VALUES(648155,'Italian Kale and Potato Soup','https://img.spoonacular.com/recipes/648155-312x231.jpg',NULL,NULL,NULL,'potato');");
        db.execSQL("INSERT INTO Recipe VALUES(648883,'Kidney Pie','https://img.spoonacular.com/recipes/648883-312x231.jpg',NULL,NULL,NULL,'potato');");
        db.execSQL("INSERT INTO Recipe VALUES(649567,'Lemon Coconut Granola','https://img.spoonacular.com/recipes/649567-312x231.jpg',NULL,NULL,NULL,'oat');");
        db.execSQL("INSERT INTO Recipe VALUES(650878,'Maple and Mustard-Glazed Salmon','https://img.spoonacular.com/recipes/650878-312x231.jpg',NULL,NULL,NULL,'salmon');");
        db.execSQL("INSERT INTO Recipe VALUES(650946,'Maple-Nut Oatmeal Cream Pies','https://img.spoonacular.com/recipes/650946-312x231.jpg',NULL,NULL,NULL,'oat');");
        db.execSQL("INSERT INTO Recipe VALUES(651715,'Mexican Three Cheese Dip','https://img.spoonacular.com/recipes/651715-312x231.jpg',NULL,NULL,NULL,'miso');");
        db.execSQL("INSERT INTO Recipe VALUES(652335,'Mongolian Beef','https://img.spoonacular.com/recipes/652335-312x231.jpg',NULL,NULL,NULL,'miso');");
        db.execSQL("INSERT INTO Recipe VALUES(652421,'Moroccan Chicken Tagine','https://img.spoonacular.com/recipes/652421-312x231.jpg',NULL,NULL,NULL,'chicken');");
        db.execSQL("INSERT INTO Recipe VALUES(654515,'Pandan Smoothie','https://img.spoonacular.com/recipes/654515-312x231.jpg',NULL,'[{\"name\":\"pandan leaves click here cut to 2\\\\\\\" length\",\"amount\":\"5.0\",\"unit\":\"\"},{\"name\":\"soda\",\"amount\":\"200.0\",\"unit\":\"ml\"},{\"name\":\"cucumber\",\"amount\":\"100.0\",\"unit\":\"grams\"},{\"name\":\"honey or\",\"amount\":\"6.0\",\"unit\":\"tablespoons\"},{\"name\":\"oranges\",\"amount\":\"3.0\",\"unit\":\"\"},{\"name\":\"water\",\"amount\":\"1.0\",\"unit\":\"tablespoon\"}]','[\"Blend pandan leaves and water together, strain.\",\"Blend oranges, cucumber, honey and soda, then add in the pandan water again and mix well.\",\"Pour \\\"Smoothie\\\" into a jug and leave to chill in the fridge before serving.\"]',NULL);");
        db.execSQL("INSERT INTO Recipe VALUES(654679,'Parmesan Mashed Potatoes','https://img.spoonacular.com/recipes/654679-312x231.jpg',NULL,NULL,NULL,'potato');");
        db.execSQL("INSERT INTO Recipe VALUES(655524,'Pecan Rice Pilaf','https://img.spoonacular.com/recipes/655524-312x231.jpg',NULL,NULL,NULL,'rice');");
        db.execSQL("INSERT INTO Recipe VALUES(657178,'Protein Packed Carrot Muffins','https://img.spoonacular.com/recipes/657178-312x231.jpg',NULL,NULL,NULL,'tofu');");
        db.execSQL("INSERT INTO Recipe VALUES(658057,'Red Currant Streusel Bars','https://img.spoonacular.com/recipes/658057-312x231.jpg',NULL,NULL,NULL,'beer');");
        db.execSQL("INSERT INTO Recipe VALUES(658760,'Rooibos Vanilla Sweet Rolls','https://img.spoonacular.com/recipes/658760-312x231.jpg',NULL,NULL,NULL,'bread');");
        db.execSQL("INSERT INTO Recipe VALUES(658782,'Rosemary and Red Onion Focaccia','https://img.spoonacular.com/recipes/658782-312x231.jpg',NULL,NULL,NULL,'bread');");
        db.execSQL("INSERT INTO Recipe VALUES(659092,'Salmon on Kiwi & Lemon Puree','https://img.spoonacular.com/recipes/659092-312x231.jpg',NULL,NULL,NULL,'salmon');");
        db.execSQL("INSERT INTO Recipe VALUES(660185,'Singapore Curry','https://img.spoonacular.com/recipes/660185-312x231.jpg',NULL,NULL,NULL,'rice');");
        db.execSQL("INSERT INTO Recipe VALUES(661427,'Spring Risotto with Shrimp, Asparagus and Artichoke Hearts','https://img.spoonacular.com/recipes/661427-312x231.jpg',NULL,NULL,NULL,'rice');");
        db.execSQL("INSERT INTO Recipe VALUES(663157,'Thai Street Vendor Salmon Skewers','https://img.spoonacular.com/recipes/663157-312x231.jpg',NULL,NULL,NULL,'salmon');");
        db.execSQL("INSERT INTO Recipe VALUES(663905,'Tuna and Tofu Cold Dish','https://img.spoonacular.com/recipes/663905-312x231.jpg',NULL,NULL,NULL,'tofu');");
        db.execSQL("INSERT INTO Recipe VALUES(664449,'Vegan Lemon-Berry \"Cheese\"Cake','https://img.spoonacular.com/recipes/664449-312x231.jpg',NULL,NULL,NULL,'tofu');");
        db.execSQL("INSERT INTO Recipe VALUES(665000,'Watercress Salad With Miso-Lime Dressing','https://img.spoonacular.com/recipes/665000-312x231.jpg',NULL,NULL,NULL,'miso');");
        db.execSQL("INSERT INTO Recipe VALUES(665403,'Wisconsin Beer Cheese Soup','https://img.spoonacular.com/recipes/665403-312x231.jpg',NULL,NULL,NULL,'beer');");
        db.execSQL("INSERT INTO Recipe VALUES(681706,'Mango Chia Smoothie Bowl','https://img.spoonacular.com/recipes/681706-312x231.png',NULL,NULL,NULL,'chia');");
        db.execSQL("INSERT INTO Recipe VALUES(841432,'Lemon Chia Seed Cornmeal Bread','https://img.spoonacular.com/recipes/841432-312x231.jpg',NULL,NULL,NULL,'chia');");
        db.execSQL("INSERT INTO Recipe VALUES(1095790,'Beer Baked Pork Chops','https://img.spoonacular.com/recipes/1095790-312x231.jpg',NULL,NULL,NULL,'beer');");
        db.execSQL("INSERT INTO Recipe VALUES(1095931,'Matcha Smoothie','https://img.spoonacular.com/recipes/1095931-312x231.jpg',NULL,NULL,NULL,'smoothie');");
        db.execSQL("INSERT INTO Recipe VALUES(1096176,'Mango, Persimmon Smoothie with Cranberries','https://img.spoonacular.com/recipes/1096176-312x231.jpg',NULL,NULL,NULL,'smoothie');");
        db.execSQL("INSERT INTO Recipe VALUES(1096260,'Baked Lemon Salmon','https://img.spoonacular.com/recipes/1096260-312x231.jpg',NULL,'[{\"name\":\"lemons\",\"amount\":\"2.0\",\"unit\":\"\"},{\"name\":\"filets of salmon\",\"amount\":\"2.0\",\"unit\":\"\"},{\"name\":\"olive oil\",\"amount\":\"2.0\",\"unit\":\"Tablespoons\"},{\"name\":\"salt and pepper\",\"amount\":\"2.0\",\"unit\":\"servings\"},{\"name\":\"thyme to garnish\",\"amount\":\"1.0\",\"unit\":\"sprigs\"}]','[\"Preheat the oven to 350F (180C).\",\"Slice lemons thinly.\",\"Divide the lemon slices in half.\",\"Take one half of the lemons and place on a sheet of foil.\",\"Put the salmon on top of the lemons and cover with the second half.\",\"Drizzle olive oil over the fillets.\",\"Fold the foil over the fillets, sealing completely.\",\"Bake in the oven for 20 minutes.\",\"Season the salmon with salt and serve garnished with thyme.\"]',NULL);");
        db.execSQL("INSERT INTO Recipe VALUES(1096302,'Coffee Chia Pudding','https://img.spoonacular.com/recipes/1096302-312x231.jpg',NULL,NULL,NULL,'chia');");
        db.execSQL("INSERT INTO Recipe VALUES(1697525,'6 Quick & Easy Smoothies To Start Your Morning','https://img.spoonacular.com/recipes/1697525-312x231.jpg',NULL,NULL,NULL,'smoothie');");
        db.execSQL("INSERT INTO MealPlan VALUES(9,'Vancouver Boost',5,'[1096260,658760,634726]',1,'[1096260]',0);");
        db.execSQL("INSERT INTO MealPlan VALUES(10,'Iced Van',6,'[654515,1697525,650946,636676]',1,NULL,0);");
        db.execSQL("INSERT INTO MealPlan VALUES(15,'BC Green',7,'[663905,644627,665000]',0,NULL,0);");
        db.execSQL("INSERT INTO MealPlan VALUES(16,'Toronto Power',8,'[635675,641836,648883]',0,NULL,0);");
        db.execSQL("INSERT INTO sqlite_sequence VALUES('User',8);");
        db.execSQL("INSERT INTO sqlite_sequence VALUES('MealPlan',16);");
    }

    /**
     * Get the Forward Sortation Area (first 3 characters) in the Postal Code
     */
    private static String getFSA(String postalCode) {
        if (postalCode == null || postalCode.length() < 3) return "";
        return postalCode.replaceAll("\\s+", "").substring(0, 3).toUpperCase();
    }

    public static RegionType getCommonRegion(String postal1, String postal2) {
        String fsa1 = getFSA(postal1);
        String fsa2 = getFSA(postal2);

        if (fsa1.equals(fsa2)) {
            return RegionType.DISTRICT;
        }

        if (!fsa1.isEmpty() && !fsa2.isEmpty() && fsa1.charAt(0) == fsa2.charAt(0)) {
            return RegionType.PROVINCE;
        }

        return RegionType.COUNTRY;
    }
}
