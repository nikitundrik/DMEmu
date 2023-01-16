package net.lockethefirst.lockesdmemu.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.lockethefirst.lockesdmemu.databinding.FragmentHomeBinding;

import java.util.Random;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    Random random;
    static float challengeRating = 0.25f;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        random = new Random();
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Button rollButton = binding.diceRollButton;
        Button encounterButton = binding.encounterButton;
        rollButton.setOnClickListener(v -> rollDice());
        encounterButton.setOnClickListener(v -> encounter());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void encounter() {
        EncounterThread thread = new EncounterThread();
        thread.start();
    }

    public void rollDice() {
        int result = random.nextInt(10) + 1;
        String resultString = "Rolled a " + result + "! ";
        if (result == 1) {
            resultString += "Critical failure!";
        } else if (result <= 3) {
            resultString += "Failure!";
        } else if (result == 4) {
            resultString += "Failure with an advantage!";
        } else if (result <= 6) {
            resultString += "Success with a disadvantage!";
        } else if (result <= 9) {
            resultString += "Success!";
        } else {
            resultString += "Critical success!";
        }
        TextView resultText = new TextView(getActivity().getApplicationContext());
        resultText.setText(resultString);
        binding.actionsLayout.addView(resultText);
    }

    public void createText(String text) {
        TextView resultText = new TextView(getActivity().getApplicationContext());
        resultText.setText(text);
        binding.actionsLayout.addView(resultText);
    }

    class EncounterThread extends Thread {

        @Override
        public void run() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.dnd5eapi.co")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            DNDService service = retrofit.create(DNDService.class);
            Call<MonsterResponse> callSync = service.getMonster("goblin");
            try {
                Response<MonsterResponse> response = callSync.execute();
                MonsterResponse response1 = response.body();
                int enemyAmount = random.nextInt(4) + 1;
                String text = enemyAmount + " " + response1;
                getActivity().runOnUiThread(() -> createText(text));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}