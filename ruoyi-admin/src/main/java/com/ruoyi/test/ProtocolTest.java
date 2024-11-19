package com.ruoyi.test;/*
 * ProtocolTest.java
 *
 * Author: Dr. Ho
 *
 * YXSmart - Java test for SE
 *
 */

import io.swagger.models.auth.In;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProtocolTest
{
    private byte[] data = null;	//������(stream)
    private int stxIndex = -1; // STX index
    private int etxIndex = -1; // ETX index
    private boolean falg = false;

    /**
     * Ĭ��constructor������ɾ��
     */
    public ProtocolTest()
    {
    }

    /**
     *
     * @param data: ������(stream)
     */
    public void setData(byte[] data)
    {
        this.data = data;
        this.findSTXandETX();
    }

    private void findSTXandETX() {
        List<Integer> stxDate = new ArrayList<>();
        List<Integer> etxDate = new ArrayList<>();
        List<int[]> stxAndEtx = new ArrayList<>();
        for (int i=0; i< data.length; i++){
            if (data[i]==2){
                stxDate.add(i);
            }
            if (data[i]==3){
                etxDate.add(i);
            }
        }
        Iterator<Integer> stxIterator = stxDate.iterator();
        while (stxIterator.hasNext()) {
            Integer i = stxIterator.next();
            if (i > 0 && data[i - 1] == 16) {
                stxIterator.remove();
            }
        }
        Iterator<Integer> etxIterator = etxDate.iterator();
        while (etxIterator.hasNext()) {
            Integer i = etxIterator.next();
            if (i > 0 && data[i - 1] == 16) {
                etxIterator.remove();
            }
        }

        //确定有效信息片段
        int i = 0, j = 0;

        boolean[] usedStx = new boolean[stxDate.size()];
        boolean[] usedEtx = new boolean[etxDate.size()];

        while (i < stxDate.size() && j < etxDate.size()) {
            // 跳过已被使用的元素
            while (i < stxDate.size() && usedStx[i]) {
                i++;
            }
            while (j < etxDate.size() && usedEtx[j]) {
                j++;
            }

            if (i >= stxDate.size() || j >= etxDate.size()) {
                break; // 如果所有元素都已经配对，则退出
            }

            // 获取当前元素
            int stxVal = stxDate.get(i);
            int etxVal = etxDate.get(j);

            // 保存这对最接近的元素
            stxAndEtx.add(new int[]{stxVal, etxVal,0});

            // 标记已使用的元素
            usedStx[i] = true;
            usedEtx[j] = true;

            // 移动指针
            if (stxVal < etxVal) {
                i++;
            } else {
                j++;
            }
        }
        //验证LRC
        for (int[] num: stxAndEtx){
            byte lrc = -1;
            // 遍历字节数组，转换有效的可见字符
            for (int g = num[0]+1 ; g <= num[1]; g++){
                // 只添加可见字符
                if (data[g] == 16 && (data[g+1] == 2 || data[g+1] == 3)){

                }else{
                    if (lrc == -1){
                        lrc = data[g];
                    }else {
                        lrc ^= data[g];
                    }
                }

            }

            if (data[num[1]+1]==lrc){
                num[2] = 1;
                falg = true;
            }else {
                num[2] = 0;
            }
        }
        for (int[] jies : stxAndEtx){
            if (jies[2] == 1){
                stxIndex = jies[0];
                etxIndex = jies[1];
            }
        }
    }

    /**
     * TODO ʵ�ָ÷���
     * @return boolean: ��������(stream)�Ƿ�����Ч��Ϣ(message)
     */
    public boolean isValid()
    {

        return falg; // Compare with LRC in the data stream
    }

    /**
     * @TODO ʵ�ָ÷���
     * @return int: ���һ����Ч���ݵĿ�ʼindex
     */
    public int getSTX()
    {
		/*
		  Write your code here...
		  .range(1, data.length - 1) // 排除 STX 和 ETX
		*/


		return stxIndex;	//���޸Ĵ���
    }

    /**
     * @TODO ʵ�ָ÷���
     * @return int: ���һ����Ч���ݵĽ���index
     */
    public int getETX()
    {
        /*
		  Write your code here...
		*/

		return etxIndex;	//���޸Ĵ���
    }
}


