package com.ing.ingterior.ui.site;

import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ing.ingterior.R;

public class ZoomableImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private Matrix matrix = new Matrix();
    private float scale = 1f;

    private ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_zoomable_image);

//        imageView = findViewById(R.id.imageView);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);

                // 이미지뷰 내에서의 터치 좌표 추출
                float[] point = {event.getX(), event.getY()};
                matrix.invert(new Matrix());
                matrix.mapPoints(point);

                // 로그에 좌표 출력
                Log.d("ZoomableImageActivity", "실제 좌표: x=" + point[0] + ", y=" + point[1]);

                // 여기서 원하는 추가 작업 수행 가능

                return true;
            }
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // 확대/축소 비율 갱신
            scale *= detector.getScaleFactor();
            // 최소 및 최대 확대 비율 제한
            scale = Math.max(0.1f, Math.min(scale, 5.0f));

            // 이미지뷰에 변환 적용
            matrix.setScale(scale, scale);
            imageView.setImageMatrix(matrix);

            return true;
        }
    }
}