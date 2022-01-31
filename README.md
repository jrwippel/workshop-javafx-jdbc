# workshop-javafx-jdbc

1) Referenciar o ScenerBuilder no Intellj
  File/Setting/Languages & Frameworks/JAVAFX
  Informar o caminho do executavel do ScenerBuilder
  C:\Users\jackson.wippel\AppData\Local\SceneBuilder\SceneBuilder.exe
  
2) Adicionar as bibliotecas javaFX
  Clicar no projeto com botão direito
  Selecionar a opção open module setting
  Selecionar Modules/dependencies
  Adicionar Library e informar o caminhodas librarys do javaFX e tambem da libray do acesso ao SQL:mysql-connector-java-8.0.27.jar
  
3) Adicionar variáveis de ambiente no Builder de execução
  Acessar o Main -> Edit Configrations
  Clicar em modify options
  Selecionar a opção ADD VM Options
  Retornar a tela de edit Configurations e no campo VM Options informar o caminho das librarys javaFX:
  --module-path C:\java-libraries\javafx-sdk-17.0.1\lib --add-modules=javafx.fxml,javafx.controls
    


