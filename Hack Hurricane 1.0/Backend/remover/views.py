from django.shortcuts import render
from  .u2net.worker import Process
from django.core.files.storage import FileSystemStorage
p = Process()
def index(request):
    if request.method == 'POST':
        image_file = request.FILES['image']
        fs = FileSystemStorage()
        filename = fs.save(image_file.name, image_file)
        p.process_single_image('media/'+filename,'media/remover_'+filename)
        processed_image_url = '/media/remover_' + filename
        return render(request, 'remover/result.html', {'processed_image_url': processed_image_url,
                                                                'transaparent':'media/transparent.jpg',
                                                                'original_image':'media/'+filename})
    return render(request, 'remover/index.html')

