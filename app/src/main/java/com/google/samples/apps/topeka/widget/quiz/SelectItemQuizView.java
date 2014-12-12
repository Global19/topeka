/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.samples.apps.topeka.widget.quiz;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.helper.AnswerHelper;
import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.quiz.SelectItemQuiz;
import com.google.samples.apps.topeka.widget.quiz.adapter.OptionsQuizAdapter;

public class SelectItemQuizView extends AbsQuizView<SelectItemQuiz>
        implements AdapterView.OnItemClickListener {

    private boolean[] mAnswers;
    private ListView mQuizContentView;

    public SelectItemQuizView(Context context, Category category, SelectItemQuiz quiz) {
        super(context, category, quiz);
        mAnswers = new boolean[quiz.getOptions().length];
    }

    @Override
    protected View getQuizContentView() {
        mQuizContentView = new ListView(getContext());
        mQuizContentView.setAdapter(
                new OptionsQuizAdapter(getQuiz().getOptions(), R.layout.item_answer));
        mQuizContentView.setOnItemClickListener(this);
        return mQuizContentView;
    }

    @Override
    protected boolean isAnswerCorrect() {
        final SparseBooleanArray checkedItemPositions = mQuizContentView.getCheckedItemPositions();
        final int[] answer = getQuiz().getAnswer();
        return AnswerHelper.isAnswerCorrect(checkedItemPositions, answer);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        allowAnswer();
        toggleAnswerFor(position);
    }

    private void toggleAnswerFor(int answerId) {
        mAnswers[answerId] = !mAnswers[answerId];
    }
}
