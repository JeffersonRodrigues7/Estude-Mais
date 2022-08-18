# Descrição
Estude Mais é um aplicativo desenvolvido para Android que auxilia
no processo de aprendizado através de uma abordagem gamificada e
fornecendo mecanismos de organização. Tanto os alunos quanto os
professores possuem uma interface própria para suprir suas necessidades,
esta que fornece um gerenciamento de cursos, atividades e alunos,
dependendo do tipo de usuário

# Detalhamento do sistema
Utilizou-se o padrão de arquitetura Model-View-ViewModel para facilitar o
desenvolvimento do sistema. Vamos dividir essa esta sessão em três partes, uma para a
camada Model que vai englobar o banco de dados, outra para a ViewModel e por fim
uma para a View.

**Camada Model**

![image](https://user-images.githubusercontent.com/40694838/185268895-517a3c47-49c5-461e-a60b-e169ce6320bf.png)

Utilizou-se a biblioteca RoomDatabse para auxiliar o gerenciamento do banco de dados.
Cada uma das entidades possuem um arquivo DAO para acessar a respectiva tabela no
banco de dados.
A RepositoryRoom é criada pela RepositoryFactory e serve como interface de
comunicação entre o banco de dados e a ViewModel. 

**ViewModel**

Há dois arquivos nessa camada. A primeira é a SessionViewModel, ela será responsável
por armazenar o id do estudante ou professor que se registrar ou fizer o login no
aplicativo, basicamente é uma forma de guardar a sessão do usuário.
A MainViewModel será responsável por fazer a comunicação entre a camada View e a
camada Model, todas os arquivos da View farão acesso ao banco de dados utilizando a
MainViewModel como intermediária.

**View**

![image](https://user-images.githubusercontent.com/40694838/185269016-563d3591-3ce9-482d-a8ae-0b901963f6a5.png)

O aplicativo têm ao todo 4 atividades, uma para login, outra para cadastro, e as outras
duas serão responsáveis por renderizar os fragmentos de alunos e professores.

**Estudante**

![image](https://user-images.githubusercontent.com/40694838/185269191-b170d77a-4c5a-4e42-9dc0-2eac3a0e16b4.png)
![image](https://user-images.githubusercontent.com/40694838/185269197-d2058ce8-c84f-437b-9efb-6706b95dfea4.png)
![image](https://user-images.githubusercontent.com/40694838/185269207-2bdaf394-4731-4d35-9279-cf81bb9314f2.png)

O estudante possui três telas principais.
A primeira que é a sua home page é a tela de “Cursos Matriculados” onde vai ficar
armazenado os cursos em que o estudante está inscrito.
Na tela de “Procurar um curso” estão todos os cursos registrados no aplicativo, e através
da SearchBar o estudante é capaz de filtrar por um conjunto de caracteres específicos.
Na tela do curso em si conseguimos ver o nome do professor, a data de início e ainda é
possível filtrar as atividades por avaliadas, não avaliadas ou todas.

**Professor**

![image](https://user-images.githubusercontent.com/40694838/185269319-7b13e188-4a10-4afa-9a7e-9852792fbbbe.png)
![image](https://user-images.githubusercontent.com/40694838/185269343-d4ef324d-423f-4b4a-b01b-1f05ef27e998.png)
![image](https://user-images.githubusercontent.com/40694838/185269335-2f6aa97e-5baf-4cf3-b887-436d7ace64e2.png)

A tela de cursos mostra todas os cursos ministrados por aquele professor.
Na tela do curso especifico é listado todas as atividades criadas, além de
funcionalidades como edição e remoção do curso por completo.
Na tela de atividade é possível visualizar e alterar a nota dos estudantes e também
atualizar ou remover a atividade em si.

# Tecnologias utilizadas
- Android Studio
- Kotlin

# O que poderia ser mudado?
Para fins de aprendizado optou-se por um banco de dados local, o ideal seria a troca por uma arquitetura cliente-servidor, utilizando por exemplo o Firebase.

# Preview
https://www.youtube.com/watch?v=XpthgXYLE6g&ab_channel=JeffersonRodrigues
