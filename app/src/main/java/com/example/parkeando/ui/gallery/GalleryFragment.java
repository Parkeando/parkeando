package com.example.parkeando.ui.gallery;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parkeando.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;


    private final int MY_PERMISSIONS_REQUEST_CAMERA = 123;


    // variable de tipo barcode
    BarcodeDetector barcodeDetector;
    //Para la camara
    CameraSource cameraSource;
    //surfaceview
    SurfaceView cameraView;

    TextView   qrResult;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of ( this ).get ( GalleryViewModel.class );
        final View root = inflater.inflate ( R.layout.fragment_gallery, container, false );
        //final TextView textView = root.findViewById ( R.id.text_gallery );
     /*  galleryViewModel.getText ().observe ( this, new Observer<String> () {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText ( s );
            }
        } ); */


        //lanzar la advertencia al iniciar la app para perdir uso de la camara
        if (ContextCompat.checkSelfPermission(getContext (), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > 22) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA))
                    Toast.makeText(getContext (), "Esta aplicación necesita acceder a la cámara para funcionar", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
        //Texto con el resultado del qr
        qrResult = (TextView) root.findViewById(R.id.resultado_qr);
        //Vista de la cámara
        cameraView = (SurfaceView) root.findViewById(R.id.camera_view);

        //Creama el lector de qr
        barcodeDetector = new BarcodeDetector.Builder(getContext ())
                .setBarcodeFormats( Barcode.QR_CODE)
                .build();

        //Creama la camara
        cameraSource = new CameraSource
                .Builder(getContext (), barcodeDetector)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //Verifica si el usuario ha dado permiso para la camara
                if (ContextCompat.checkSelfPermission(getContext (),  android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException ie) {
                                                                                                                                                                                                                                                                                                                                                                                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }
            //Detencion del surface
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
        //Detector de qr
        //Detector de qr
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() { }
            //Recibe las detecciones
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
//Establece el valor del qr en el textview
                    qrResult.post(new Runnable() {
                        public void run() {
                            //Es importe que al imprimir el resultado este sea usando el displayvalue ya que asi
                            //referencia el indice en su valor real
                            qrResult.setText(barcodes.valueAt(0).displayValue.toString());

                            String resultadoQR = barcodes.valueAt(0).displayValue.toString ();

                        }

                    });
//Cierra el detector de códigos
                    barcodeDetector.release();
                }

            }
        });


        return root;
    }
}