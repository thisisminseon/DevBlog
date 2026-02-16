/* ============================================
   DevBlog - Main JavaScript
   ============================================ */

// ---- Search bar toggle ----
function toggleSearch() {
    const bar = document.getElementById('searchBar');
    if (bar) {
        bar.classList.toggle('active');
        if (bar.classList.contains('active')) {
            bar.querySelector('input').focus();
        }
    }
}

// ---- Like toggle (AJAX) ----
function toggleLike(postId) {
    fetch('/api/like/toggle/' + postId, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(function(res) { return res.json(); })
    .then(function(data) {
        var btn = document.getElementById('likeBtn');
        var countEl = document.getElementById('likeCount');
        if (btn && countEl) {
            countEl.textContent = data.count;
            if (data.liked) {
                btn.classList.add('liked');
                btn.querySelector('.like-icon').textContent = '\u2764';
            } else {
                btn.classList.remove('liked');
                btn.querySelector('.like-icon').textContent = '\u2661';
            }
        }
    })
    .catch(function(err) {
        alert('\u30ed\u30b0\u30a4\u30f3\u304c\u5fc5\u8981\u3067\u3059\u3002');
    });
}

// ---- Comment delete (AJAX) ----
function deleteComment(commentId) {
    if (!confirm('\u30b3\u30e1\u30f3\u30c8\u3092\u524a\u9664\u3057\u3066\u3082\u3088\u308d\u3057\u3044\u3067\u3059\u304b\uff1f')) return;

    fetch('/comment/delete/' + commentId, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(function(res) { return res.json(); })
    .then(function(data) {
        if (data.success) {
            var el = document.getElementById('comment-' + commentId);
            if (el) el.remove();
        } else {
            alert(data.message || '\u524a\u9664\u306b\u5931\u6557\u3057\u307e\u3057\u305f\u3002');
        }
    });
}

// ---- Comment edit (inline) ----
function editComment(commentId) {
    var contentEl = document.getElementById('comment-content-' + commentId);
    var actionsEl = document.getElementById('comment-actions-' + commentId);
    if (!contentEl) return;

    var currentText = contentEl.textContent.trim();
    contentEl.innerHTML = '<textarea id="edit-textarea-' + commentId + '" style="width:100%;min-height:60px;padding:8px;border:1px solid #ddd;border-radius:6px;font-size:14px;font-family:inherit;">' + currentText + '</textarea>' +
        '<div style="margin-top:8px;display:flex;gap:6px;">' +
        '<button class="btn btn-primary btn-sm" onclick="saveComment(' + commentId + ')">\u4fdd\u5b58</button>' +
        '<button class="btn btn-secondary btn-sm" onclick="cancelEditComment(' + commentId + ', \'' + encodeURIComponent(currentText) + '\')">\u30ad\u30e3\u30f3\u30bb\u30eb</button>' +
        '</div>';
}

function saveComment(commentId) {
    var textarea = document.getElementById('edit-textarea-' + commentId);
    if (!textarea) return;

    var formData = new URLSearchParams();
    formData.append('content', textarea.value);

    fetch('/comment/edit/' + commentId, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
    })
    .then(function(res) { return res.json(); })
    .then(function(data) {
        if (data.success) {
            var contentEl = document.getElementById('comment-content-' + commentId);
            contentEl.textContent = textarea.value;
        } else {
            alert(data.message || '\u7de8\u96c6\u306b\u5931\u6557\u3057\u307e\u3057\u305f\u3002');
        }
    });
}

function cancelEditComment(commentId, encodedText) {
    var contentEl = document.getElementById('comment-content-' + commentId);
    if (contentEl) {
        contentEl.textContent = decodeURIComponent(encodedText);
    }
}

// ---- Tag input widget ----
function initTagInput() {
    var wrapper = document.getElementById('tagInputWrapper');
    var hiddenInput = document.getElementById('tagsHidden');
    var input = document.getElementById('tagInputField');
    if (!wrapper || !hiddenInput || !input) return;

    // Load existing tags
    var existingTags = hiddenInput.value ? hiddenInput.value.split(',').map(function(t) { return t.trim(); }).filter(Boolean) : [];
    existingTags.forEach(function(tag) { addTagChip(tag); });

    input.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' || e.key === ',') {
            e.preventDefault();
            var val = input.value.replace(/,/g, '').trim();
            if (val) {
                addTagChip(val);
                input.value = '';
                updateHiddenInput();
            }
        }
        if (e.key === 'Backspace' && input.value === '') {
            var chips = wrapper.querySelectorAll('.tag-chip');
            if (chips.length > 0) {
                chips[chips.length - 1].remove();
                updateHiddenInput();
            }
        }
    });

    wrapper.addEventListener('click', function() { input.focus(); });

    function addTagChip(text) {
        text = text.replace(/^#/, '').trim();
        if (!text) return;

        // Prevent duplicates
        var existing = wrapper.querySelectorAll('.tag-chip');
        for (var i = 0; i < existing.length; i++) {
            if (existing[i].getAttribute('data-tag') === text) return;
        }

        var chip = document.createElement('span');
        chip.className = 'tag-chip';
        chip.setAttribute('data-tag', text);
        chip.innerHTML = '#' + text + ' <span class="remove-tag" onclick="this.parentElement.remove(); updateHiddenInput();">\u00d7</span>';
        wrapper.insertBefore(chip, input);
    }
}

function updateHiddenInput() {
    var wrapper = document.getElementById('tagInputWrapper');
    var hiddenInput = document.getElementById('tagsHidden');
    if (!wrapper || !hiddenInput) return;

    var chips = wrapper.querySelectorAll('.tag-chip');
    var tags = [];
    chips.forEach(function(chip) { tags.push(chip.getAttribute('data-tag')); });
    hiddenInput.value = tags.join(', ');
}

// ---- Image preview ----
function previewImages(input) {
    var grid = document.getElementById('imagePreviewGrid');
    if (!grid) return;
    grid.innerHTML = '';

    if (input.files) {
        Array.from(input.files).forEach(function(file, index) {
            var reader = new FileReader();
            reader.onload = function(e) {
                var div = document.createElement('div');
                div.className = 'image-preview-item';
                div.innerHTML = '<img src="' + e.target.result + '" alt="preview">';
                grid.appendChild(div);
            };
            reader.readAsDataURL(file);
        });
    }
}

// ---- Comment image preview ----
function previewCommentImage(input) {
    var preview = document.getElementById('commentImagePreview');
    if (!preview) return;

    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function(e) {
            preview.innerHTML = '<img src="' + e.target.result + '" alt="preview" style="max-width:200px;border-radius:8px;margin-top:8px;">';
        };
        reader.readAsDataURL(input.files[0]);
    } else {
        preview.innerHTML = '';
    }
}

// ---- Post delete confirmation ----
function confirmDelete(postId) {
    if (confirm('\u3053\u306e\u6295\u7a3f\u3092\u524a\u9664\u3057\u3066\u3082\u3088\u308d\u3057\u3044\u3067\u3059\u304b\uff1f')) {
        document.getElementById('deleteForm-' + postId).submit();
    }
}

// ---- Account delete confirmation ----
function confirmDeleteAccount() {
    if (confirm('\u672c\u5f53\u306b\u30a2\u30ab\u30a6\u30f3\u30c8\u3092\u524a\u9664\u3057\u3066\u3082\u3088\u308d\u3057\u3044\u3067\u3059\u304b\uff1f\u3053\u306e\u64cd\u4f5c\u306f\u5143\u306b\u623b\u305b\u307e\u305b\u3093\u3002')) {
        document.getElementById('deleteAccountForm').submit();
    }
}

// ---- Init on page load ----
document.addEventListener('DOMContentLoaded', function() {
    initTagInput();
});
