package com.jianboke.comparator;

import java.util.Comparator;

import com.jianboke.model.ChapterModel;

/**
 * 对列表就某个integer类型的属性进行排序的方法。继承Comparator接口，实现compare方法即可
 * @author pengxg
 *
 */
public class ChapterModelComparator implements Comparator<ChapterModel> {

	@Override
	public int compare(ChapterModel model1, ChapterModel model2) {
		if(model1 == null || model2 ==null){
            return 1;
        }
		return model1.getSortNum().compareTo(model2.getSortNum());
	}

}
