package giulietta.service;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class ClassifierHelperTest {

	@Test
	public void test() {
		assertCorrect(getList(1,2), getList(1),"missing");
		assertCorrect(getList(1,2), getList(2),"missing");
		assertCorrect(getList(1,2), getList(3),"error");
		assertCorrect(getList(1,2), getList(1,2),"good");
		assertCorrect(getList(1,2), getList(1,3),"invalid");
		assertCorrect(getList(1,2), getList(2,3),"invalid");
	
		
		assertCorrect(getList(1), getList(1),"good");
		assertCorrect(getList(1), getList(2),"error");
		assertCorrect(getList(1), getList(3),"error");
		assertCorrect(getList(1), getList(1,2),"invalidplus");
		assertCorrect(getList(1), getList(1,3),"invalidplus");
		assertCorrect(getList(1), getList(2,3),"errorplus");
		

	}
	
	private void assertCorrect(List<Integer> reponses,List<Integer> correction,String value){
		assertEquals(value,ClassifierHelper.getAnswerStatus(reponses,correction));
	}
	
	private List<Integer> getList(Integer... integers ){
		List<Integer> result = new ArrayList<Integer>();
		for (Integer inte : integers){
			result.add(inte);
		}
		return result;
	}

}
