package giulietta.service;

import java.util.List;

public class ClassifierHelper {


	public static String getAnswerStatus(List<Integer> reponses,List<Integer> reponsesEleve){
		System.out.println(reponses);
		System.out.println(reponsesEleve);
		System.out.println("**********");
		int good=0;
		for (Integer reponseEleve : reponsesEleve){
			if (reponses.contains(reponseEleve)){
				++good;
			}
		}
		int nreponsesEleve = reponsesEleve.size();
		int nreponsesAttendues = reponses.size();
		
		
		int missing = nreponsesAttendues - good;
		int bad = nreponsesEleve - good;
		
		if (nreponsesAttendues==1){
			assert missing == 0;
			if (good ==1 && bad == 0){
				return "good";
			}
			if (good ==1 && bad == 1){
				return "invalidplus";
			}
			if (good ==0 && bad == 1){
				return "error";
			}
			if (good ==0 && bad == 2){
				return "errorplus";
			}
			if (nreponsesEleve == 3){
				return "errorplus";
			}
		}
		assert nreponsesAttendues==2;
		if (good ==2 && bad == 0){
			return "good";
		}
		if (good ==1 && missing == 1 && bad == 0){
			assert nreponsesEleve == 1;
			return "missing";
		}
		if (good ==1 && bad == 1){
			return "invalid";
		}
		if (good ==0 && bad == 1){
			return "error";
		}
		if (nreponsesEleve == 3){
			return "errorplus";
		}
		System.out.println("error");
		return "errorplus";
		
	}

}
