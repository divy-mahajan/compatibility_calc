package engine;
public class Question{
    private String id;
    private String text;
    private String[] options;
    private int[] values;
    private String dimension;

    public Question(String id,String text,String[] options,int[] values,String dimension){
        this.id =id;
        this.text=text;
        this.options=options;
        this.values=values;
        this.dimension=dimension;
    }
    public String[] getOptions(){
        return  options;
    }
    public int getValueForOption(int index){
        return values[index];
    }
    public String getDimension(){
        return dimension;
    }
    public String getId() {
        return id;
    }
    public String getText() {
        return text;
    }
}