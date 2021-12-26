package com.example.tummyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipesDetailsActivity extends AppCompatActivity {

    private TextView ingredientsDetails;
    private TextView preparationDetails;
    private ImageView detailsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ingredientsDetails = findViewById(R.id.ingredientsDetails);
        preparationDetails = findViewById(R.id.preparationDetails);
        detailsImage = findViewById(R.id.detailsImage);

        String t = "1 cup butter (230 g), softened\n" +
                "7 cloves garlic, minced\n" +
                "2 tablespoons fresh rosemary, finely chopped\n" +
                "2 tablespoons fresh thyme, finely chopped\n" +
                "2 tablespoons salt\n" +
                "1 tablespoon pepper\n" +
                "5 lb boneless rib eye roast (2.2 kg), trimmed\n" +
                "2 tablespoons flour\n" +
                "2 cups beef stock (475 mL)\n" +
                "mashed potato, to serve\n" +
                "green bean, to serve";
        String t1 = "Preheat oven to 500 °F (260 °C).\n" +
                "Mix together the butter, garlic, herbs, salt, and pepper in a bowl until evenly combined.\n" +
                "Rub the herb butter all over the rib roast, then place it on a roasting tray with a rack.\n" +
                "Bake for 5 minutes per pound (10 minutes per kilo) of meat, so a 5-pound (2.2 kg) roast would bake for 25 minutes.\n" +
                "Turn off the heat and let the rib roast sit in the oven for 2 hours, making sure you do not open the oven door, or else the residual heat will escape.\n" +
                "Once the 2 hours are up, remove the roast from the pan and pour the pan drippings into a saucepan over medium heat.\n" +
                "Add the flour, whisking until there are no lumps, then add the beef stock, stirring and bringing the sauce to a boil.\n" +
                "Remove from heat and strain the sauce into a gravy dish.\n" +
                "Carve the prime rib into 20 mm slices.\n" +
                "Serve with the mashed potatoes, green beans, and sauce!\n" +
                "Enjoy!";

        String t2 = "★ ";
        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i) == '\n') {
                t2 += t.charAt(i) + "\n" + "★ ";
            } else {
                t2 += t.charAt(i);
            }
        }

        String t3 = "★ ";
        for (int i = 0; i < t1.length(); i++) {
            if (t1.charAt(i) == '\n') {
                t3 += t1.charAt(i) + "\n" + "★ ";
            } else {
                t3 += t1.charAt(i);
            }
        }

        ingredientsDetails.setText(t2);
        preparationDetails.setText(t3);

        detailsImage.setImageResource(R.drawable.recipe_20);
    }
}