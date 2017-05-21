/**
 * Created by yjfu on 2017/4/19.
 */
public class FuncAdd implements Function {
    public String func(){
        boolean league = true;
        if(ParameterProcasser.paraList.containsKey("a")&&ParameterProcasser.paraList.containsKey("b")){
            String a = ParameterProcasser.paraList.get("a");
            String b = ParameterProcasser.paraList.get("b");
            double  da,db;
            try{
                da = Double.parseDouble(a);
                db = Double.parseDouble(b);
            }
            catch(Exception e){
                league = false;
                return ParameterProcasser.makeHTML("Wrong Parameter!");
            }
            return ParameterProcasser.makeHTML("equal to"+(da+db));
        }
        return ParameterProcasser.makeHTML("Wrong Parameter!");
    }
}
