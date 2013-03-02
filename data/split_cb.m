
base_name='mknapcb8';
extension='.txt';

%Lectura archivo
fid=fopen([base_name extension]);
data=textscan(fid,'%d');
fclose(fid);
data=data(1);

%Lectura archivo valores optimos
fid=fopen('mkcbres.txt');
opt=textscan(fid,'%s%d');
fclose(fid);

instance_name=opt{1};
instance_opt=opt{2};

%Convierto en vector el cell
data=data{1};

%Numero de instancias
instances=data(1);


index=2;

for i=0:instances-1
    
    n_elements=data(index);
    index=index+1;
    n_constraints=data(index);
    index=index+1;
    
    
    %Me salto la solucion que siempre vale 0
    index=index+1;
    
    % Performances
    fin=index+n_elements-1;
    performances=data(index:fin);    
    index=fin+1;
    
    %Costes
    fin=index + n_elements*n_constraints - 1;
    costs=data(index:fin);
    index=fin+1;
    
    costs=reshape(costs,n_elements,n_constraints);
    costs=costs';
    
    %Restricciones
    fin=index + n_constraints - 1;
    constraints=data(index:fin);
    index=fin+1;
    
    %Buscar valor optimo
    optimal=0;
    instance=sprintf('%d.%d-',n_constraints,n_elements);
    
    if i<10
        instance=[instance '0'];
    end
    instance=[instance num2str(i)];
       
    for j=1:size(instance_name)
        if strcmp(instance_name(j),instance)
            optimal=instance_opt(j);
            break;
        end
    end
    
    %Escribir en archivo salida
    l=length(instance);
    out=[base_name '_' instance(l-1:l) '.dat'];
    
    %Formato archivo salida
    %Restricciones elementos
    %Performances
    %Constraints
    %Costs
    %Linea en blanco
    %Valor optimo
    
    dlmwrite(out,[n_constraints n_elements],' ');
    dlmwrite(out,performances','-append','delimiter',' ','precision','%d');
    dlmwrite(out,constraints','-append','delimiter',' ','precision','%d');
    dlmwrite(out,costs,'-append','delimiter',' ','precision','%d');
    dlmwrite(out,' ','-append','delimiter',' ');
    dlmwrite(out,optimal,'-append','delimiter',' ','precision','%d');   

end

    
    
    
    

