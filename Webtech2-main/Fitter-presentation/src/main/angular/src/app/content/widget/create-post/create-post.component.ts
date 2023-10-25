import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TimlimePostComponent } from '../timlime-post/timlime-post.component';
import { TimelineComponent } from '../../pages/timeline/timeline.component';
import { TimelineService } from '../timlime-post/timeline-post.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.sass'],
})
export class CreatePostComponent implements OnInit {
  constructor(
    private http: HttpClient,
    private timelineService: TimelineService
  ) {}

  postForm!: FormGroup;
  hasAlreadyPoste: boolean = false;

  ngOnInit(): void {
    this.postForm = new FormGroup({
      steps: new FormControl(null, Validators.required),
      description: new FormControl(null),
    });
  }

  onSubmit() {
    if (this.postForm.valid) {
      console.log('Your post: ', this.postForm.value);

      const url = 'http://ls5vs019.cs.tu-dortmund.de:9001/api/v1/post'; // Replace with your API URL
      const body = {
        steps: this.postForm.value.steps,
        description: this.postForm.value.description,
      };

      const options = {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
        }),
        withCredentials: true,
      };

      this.http.post(url, body, options).subscribe({
        next: (response) => {
          // Handle response here
          console.log(response);
        },
        error: (error) => {
          console.log(error);
          this.hasAlreadyPoste = true;
        },
        complete: () => {
          // Code to be executed once the Observable completes
          console.log('Completed');
          this.timelineService.refreshTimeline();
        },
      });

      this.postForm.reset();
    } else {
      console.error('Form is invalid');
    }
  }
}
