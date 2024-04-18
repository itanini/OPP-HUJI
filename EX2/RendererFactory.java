class RendererFactory{
    public Renderer buildRenderer(String type, int size){
        if (type.equals("console")){
            return new ConsoleRenderer(size);
        }
        return new VoidRenderer();
    }
}
