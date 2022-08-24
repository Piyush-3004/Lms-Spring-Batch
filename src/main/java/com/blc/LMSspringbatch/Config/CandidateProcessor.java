package com.blc.LMSspringbatch.Config;

import com.blc.LMSspringbatch.Model.CandidateModel;
import org.springframework.batch.item.ItemProcessor;

public class CandidateProcessor implements ItemProcessor<CandidateModel,CandidateModel >{

    @Override
    public CandidateModel process(CandidateModel candidateModel) throws Exception {
     //   if(candidateModel.getCountry().equals("United States")) {
            return candidateModel;
    //    }else{
     //       return null;
      //  }
    }	

}
