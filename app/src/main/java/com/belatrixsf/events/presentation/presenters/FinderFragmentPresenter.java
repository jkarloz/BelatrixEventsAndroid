/* The MIT License (MIT)
* Copyright (c) 2016 BELATRIX
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package com.belatrixsf.events.presentation.presenters;

import com.belatrixsf.events.domain.interactors.GetPersonByQRInteractor;
import com.belatrixsf.events.presentation.presenters.base.BelatrixBasePresenter;
import com.belatrixsf.events.presentation.presenters.base.BelatrixBaseView;
import javax.inject.Inject;

/**
 * Created by dvelasquez on 06/04/2017.
 */
public class FinderFragmentPresenter extends BelatrixBasePresenter<FinderFragmentPresenter.View> {

    public interface View extends BelatrixBaseView {
        void onResult();
    }

    @Inject
    GetPersonByQRInteractor getPersonByQRInteractor;

    @Inject
    public FinderFragmentPresenter(GetPersonByQRInteractor interactor) {
        this.getPersonByQRInteractor = interactor;
    }

    public void actionFindPerson(final String qrCode){
        view.showProgressIndicator();
        getPersonByQRInteractor.execute(new GetPersonByQRInteractor.CallBack() {
            @Override
            public void onSuccess() {
                view.hideProgressIndicator();
                view.onResult();
            }

            @Override
            public void onError() {
                view.hideProgressIndicator();
            }
        }, GetPersonByQRInteractor.Params.forQR(qrCode));
    }



    @Override
    public void cancelRequests() {

    }
}