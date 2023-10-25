import { Component, Input, OnInit } from '@angular/core';
import {
  Post,
  PostResponse,
  UserService,
  UserProfile,
} from '../post/post.service';
import { Observable } from 'rxjs';
import { AdminPostService } from './admin-post.service';
import { EmojiComponent } from '@ctrl/ngx-emoji-mart/ngx-emoji';
import { AppComponent } from 'src/app/app.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-admin-post',
  templateUrl: './admin-post.component.html',
  styleUrls: ['./admin-post.component.sass'],
})
export class AdminPostComponent {
  @Input() username: string | null = null;
  posts$: Post[] = [];
  showEmojiPicker: boolean = false;
  postToEdit: Post | null = null;
  editPostForm!: FormGroup;

  constructor(
    private adminPostService: AdminPostService,
    private userService: UserService,
    private appComponent: AppComponent
  ) {}

  ngOnInit(): void {
    this.editPostForm = new FormGroup({
      steps: new FormControl(null, Validators.required),
      description: new FormControl(null),
    });
    // this.adminPostService.refreshTimeline();
    this.refreshAdminPosts();
  }

  addEmoji(event: any, postID: number) {
    console.log(event.emoji.native);
    this.userService.addReaktion(postID, event.emoji.native).subscribe({
      next: (response) => {
        console.log('Reaction added', response);
        this.refreshAdminPosts();
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }

  editPost(postID: number) {
    this.postToEdit = this.posts$.find((post) => post.id === postID) || null;

    if (this.postToEdit) {
      // Pre-fill the form with the current post data
      this.editPostForm?.setValue({
        steps: this.postToEdit.steps,
        description: this.postToEdit.description,
      });
    }
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
          this.refreshAdminPosts();
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

  refreshAdminPosts() {
    this.userService.getAllPosts().subscribe({
      next: (response: PostResponse) => {
        this.posts$ = response.posts.map((post) => ({
          ...post,
          showEmojiPicker: false,
        }));
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }

  refreshPosts(username: string) {
    this.userService.getPosts(username).subscribe({
      next: (response: PostResponse) => {
        this.posts$ = response.posts.map((post) => ({
          ...post,
          showEmojiPicker: false,
        }));
      },
      error: (error) => {
        console.error('There was an error!', error);
      },
    });
  }

  //TODO: Delete Post
  deletePost(postID: number) {
    this.userService.removePost(postID).subscribe({
      next: (response) => {
        console.log('Reaction added', response);
        this.refreshAdminPosts();
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
        this.refreshAdminPosts();
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
