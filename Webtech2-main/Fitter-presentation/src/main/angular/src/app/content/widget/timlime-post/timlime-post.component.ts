import { Component } from '@angular/core';
import { Post, PostResponse, UserService } from '../post/post.service';
import { Observable, map } from 'rxjs';
import { TimelineService } from './timeline-post.service';
import { AppComponent } from 'src/app/app.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-timlime-post',
  templateUrl: './timlime-post.component.html',
  styleUrls: ['./timlime-post.component.sass'],
})
export class TimlimePostComponent {
  posts$: Observable<Post[]>;

  postToEdit: Post | null = null;
  editPostForm!: FormGroup;

  constructor(
    private timelineService: TimelineService,
    private userService: UserService,
    private appComponent: AppComponent
  ) {
    this.posts$ = this.timelineService.posts;
  }

  ngOnInit(): void {
    this.editPostForm = new FormGroup({
      steps: new FormControl(null, Validators.required),
      description: new FormControl(null),
    });

    this.timelineService.refreshTimeline();
  }

  editPost(postID: number) {
    this.posts$
      .pipe(map((posts) => posts.find((post) => post.id === postID)))
      .subscribe((post) => {
        this.postToEdit = post || null;
        if (this.postToEdit) {
          // Pre-fill the form with the current post data
          this.editPostForm?.setValue({
            steps: this.postToEdit.steps,
            description: this.postToEdit.description,
          });
        }
      });
  }

  updatePost() {
    if (this.postToEdit && this.editPostForm?.valid) {
      const updatedPost = { ...this.postToEdit, ...this.editPostForm.value };
      this.userService.updatePost(updatedPost).subscribe({
        next: (response) => {
          // Handle response here
          console.log(response);
        },
        error: (error) => {
          console.log(error);
        },
        complete: () => {
          // Code to be executed once the Observable completes
          console.log('Completed');
          this.postToEdit = null;
          this.timelineService.refreshTimeline();
        },
      });
    }
  }

  cancelEditing() {
    this.postToEdit = null;
  }

  isAdmin(): boolean {
    return this.appComponent.user?.admin ?? false;
  }

  getCurrentUsername(): string {
    return this.appComponent.user?.username ?? '';
  }

  deletePost(postID: number) {
    this.userService.removePost(postID).subscribe({
      next: (response) => {
        console.log('Reaction added', response);
        this.timelineService.refreshTimeline();
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }

  addEmoji(event: any, postID: number) {
    console.log(event.emoji.native);
    this.userService.addReaktion(postID, event.emoji.native).subscribe({
      next: (response) => {
        console.log('Reaction added', response);
        this.timelineService.refreshTimeline();
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }

  removeEmoji(postID: number) {
    this.userService.removeReaktion(postID).subscribe({
      next: (response) => {
        console.log('Reaction added', response);
        this.timelineService.refreshTimeline();
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }

  getReactionArray(reactions: {
    [key: string]: number;
  }): Array<{ key: string; value: number }> {
    let reactionArray = [];
    for (let key in reactions) {
      reactionArray.push({ key: key, value: reactions[key] });
    }
    return reactionArray;
  }
}
