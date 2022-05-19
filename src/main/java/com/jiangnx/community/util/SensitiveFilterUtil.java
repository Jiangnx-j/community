package com.jiangnx.community.util;

import org.apache.commons.lang3.CharUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilterUtil {

    //根节点
    private TreeNode root = new TreeNode();

    //定义将敏感词替换
    private static final String REPLACEWORD = "**";


    //初始化前缀树
    @PostConstruct
    private void init() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitiveKwords.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String keyWord = null;
        while ((keyWord = bufferedReader.readLine()) != null){
            addSubNode(keyWord);
        }
    }

    private void addSubNode(String keyWord){
        TreeNode pNode = root;
        for (int i = 0; i<keyWord.length();i++){
            char c = keyWord.charAt(i);
            //查看当前节点下有没有该关键字，若是没有，就新建一个节点
            TreeNode subNode = pNode.getSubNodes(c);
            if(subNode == null){
                subNode = new TreeNode();
                pNode.setSubNodes(c, subNode);
            }
            pNode = subNode;
        }
        //设置结束标识
        pNode.setKeyWrodEnd(true);
    }

    public String doFilter(String text){
        //用于表明前缀树当前搜索的位置
        TreeNode pNode = root;
        //文本String中的前指针
        int preIndex = 0;
        //文本String中的后指针
        int offsetIndex = 0;
        //用于保存过考虑后的字符串
        StringBuilder builder = new StringBuilder();
        while (offsetIndex < text.length()){
            char c = text.charAt(offsetIndex);
            //判断是否是特殊符号
            if (isSymbool(c)){
                //当判断的符号是特殊字符时。不需要对比，如果pNode在根节点，说明还没有开始对比词语，就直接去对比下一个字符
                if (pNode == root){
                    builder.append(text.charAt(preIndex));
                    ++preIndex;
                }
                //若是不在根节点，说明正在对比词语，应该将offsetIndex进行移动
                offsetIndex++;
                continue;
            }


            //若果不是特殊字符，就与pNode的下级结点进行对比，三种情况：没有子节点，重置指针。是尾结点，替换敏感词。不是尾结点，继续向下对比。
            pNode = pNode.getSubNodes(c);
            //若果c不在pNode的下级节点中，说明c不是敏感词
            if (pNode == null){
                builder.append(text.charAt(preIndex));
                //对比结束，重新调整指针位置
                preIndex++;
                offsetIndex = preIndex;
                pNode = root;
                //有子节点，且是尾结点说明发现敏感词
            }else if(pNode.isKeyWrodEnd()){
                builder.append(REPLACEWORD);
                offsetIndex++;
                preIndex = offsetIndex;
                pNode = root;
            }else {
                offsetIndex++;
            }
        }
        builder.append(text.substring(preIndex,offsetIndex));
        return builder.toString();
    }

    private boolean isSymbool(char c){
        //isAsciiAlphanumeric(c) 判断字符是否是英文字符, 0x2e80 - 0x9FFF 是东亚字符范围
        //在东亚字符范围之外，且不是英文字符，判断为特殊字符
        if (!CharUtils.isAsciiAlphanumeric(c)&& (c < 0x2e80 || c > 0x9FFF)){
            return true;
        }
        return false;
    }
    //定义前缀树结构
    private class TreeNode{
        //是否是尾结点
        private boolean isKeyWrodEnd = false;

        //子节点
        private Map<Character,TreeNode> subNodes = new HashMap<>();

        public boolean isKeyWrodEnd() {
            return isKeyWrodEnd;
        }

        public void setKeyWrodEnd(boolean keyWrodEnd) {
            isKeyWrodEnd = keyWrodEnd;
        }

        public void setSubNodes(Character character,TreeNode node){
            subNodes.put(character,node);
        }

        public TreeNode getSubNodes(Character character){
            return subNodes.get(character);
        }
    }
}
