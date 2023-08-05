package com.example.noteitdown.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.noteitdown.Functions.FirebaseOperations;
import com.example.noteitdown.Model.Notes;
import com.example.noteitdown.Model.User;
import com.example.noteitdown.R;
import com.example.noteitdown.ViewModel.NotesViewModel;
import com.example.noteitdown.databinding.ActivityAddNotesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import kotlin.UByteArray;

public class AddNotesActivity extends AppCompatActivity {

    ActivityAddNotesBinding binding;
    String Title,SubTitle,Note;
    ImageView OCR,Share,Gallery;
    NotesViewModel notesViewModel;
    String priority = "";
    private Bitmap bitmap;


    private TextRecognizer recognizer;

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCaptureLauncher.launch(takePictureIntent);
    }

    private final ActivityResultLauncher<Intent> mCaptureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle extras = result.getData().getExtras();
                    bitmap = (Bitmap) extras.get("data");
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.OCRCameraCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    mCaptureLauncher.launch(takePictureIntent);
                }
                int rotationDegree = 0;
                InputImage image = InputImage.fromBitmap(bitmap, rotationDegree);
                recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                Task<Text> result =
                        recognizer.process(image)
                                .addOnSuccessListener(new OnSuccessListener<Text>() {
                                    @Override
                                    public void onSuccess(Text visionText) {
                                        // Task completed successfully
                                        // ...
                                        binding.NoteAddNotes.setText(visionText.getText());
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                System.out.println(e.getMessage());
                                            }
                                        });
                String resultText = String.valueOf(result.getResult());
                for (Text.TextBlock block : result.getResult().getTextBlocks()) {
                    String blockText = block.getText();
                    Point[] blockCornerPoints = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    for (Text.Line line : block.getLines()) {
                        String lineText = line.getText();
                        Point[] lineCornerPoints = line.getCornerPoints();
                        Rect lineFrame = line.getBoundingBox();
                        for (Text.Element element : line.getElements()) {
                            String elementText = element.getText();
                            Point[] elementCornerPoints = element.getCornerPoints();
                            Rect elementFrame = element.getBoundingBox();
                            for (Text.Symbol symbol : element.getSymbols()) {
                                String symbolText = symbol.getText();
                                Point[] symbolCornerPoints = symbol.getCornerPoints();
                                Rect symbolFrame = symbol.getBoundingBox();
                            }
                        }
                    }
                }

            }
        });




        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        binding.greenLowPriorityOption.setOnClickListener(v ->{
            priority  = "1";
            binding.redHighPriorityOption.setImageResource(0);
            binding.yellowMediumPriorityOption.setImageResource(0);
            binding.greenLowPriorityOption.setImageResource(R.drawable.done_icon);
            Toast.makeText(getApplicationContext(),priority,Toast.LENGTH_SHORT).show();
        });


        binding.yellowMediumPriorityOption.setOnClickListener(v ->{
            priority  = "2";
            binding.greenLowPriorityOption.setImageResource(0);
            binding.redHighPriorityOption.setImageResource(0);
            binding.yellowMediumPriorityOption.setImageResource(R.drawable.done_icon);
            Toast.makeText(getApplicationContext(),priority,Toast.LENGTH_SHORT).show();
        });


        binding.redHighPriorityOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority = "3";
                binding.greenLowPriorityOption.setImageResource(0);
                binding.yellowMediumPriorityOption.setImageResource(0);
                binding.redHighPriorityOption.setImageResource(R.drawable.done_icon);
                Toast.makeText(getApplicationContext(),priority,Toast.LENGTH_SHORT).show();
            }
        });



        binding.NoteDoneAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = binding.TitleAddNotes.getText().toString();
                SubTitle = binding.SubtitleAddNotes.getText().toString();
                Note = binding.NoteAddNotes.getText().toString();
                CreateANote(Title,SubTitle,Note,priority);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            private void CreateANote(String title, String subTitle, String notes, String priority) {
                Date date = new Date();
                CharSequence sequence = DateFormat.getTimeInstance().format(date);
                Notes note = new Notes(title,subTitle,sequence.toString(),priority,notes);
                notesViewModel.insertNote(note);
                if(User.CloudState) {
                    FirebaseOperations.AddNoteOnline(note,getApplicationContext(),false);
                    FirebaseOperations.SyncNotes(getApplicationContext());
                }
                Toast.makeText(getApplicationContext(),"Note created Successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }
}